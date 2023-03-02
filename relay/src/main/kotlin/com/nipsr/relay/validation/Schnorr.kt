package com.nipsr.relay.validation

import com.nipsr.relay.validation.Hashing.sha256
import java.math.BigInteger

/**
 * A Schnorr signature implementation.
 *
 * Based on https://code.samourai.io/samouraidev/BIP340_Schnorr/-/tree/develop/src/com/samourai/wallet/schnorr
 */
object Schnorr {

    fun verify(msg: ByteArray, pubkey: ByteArray, sig: ByteArray): Boolean = try {
        if (msg.size != 32) {
            throw IllegalArgumentException("The message must be a 32-byte array.")
        }
        if (pubkey.size != 32) {
            throw IllegalArgumentException("The public key must be a 32-byte array.")
        }
        if (sig.size != 64) {
            throw IllegalArgumentException("The signature must be a 64-byte array.")
        }
        val P: Point = liftX(pubkey)
        val r: BigInteger = sig.copyOfRange(0, 32).toBigInteger()
        val s: BigInteger = sig.copyOfRange(32, 64).toBigInteger()
        if (r >= Point.p || s >= Point.n) {
            throw IllegalArgumentException("r or s is not in range")
        }
        val len = 32 + pubkey.size + msg.size
        val buf = ByteArray(len)
        System.arraycopy(sig, 0, buf, 0, 32)
        System.arraycopy(pubkey, 0, buf, 32, pubkey.size)
        System.arraycopy(msg, 0, buf, 32 + pubkey.size, msg.size)
        val e: BigInteger = taggedHash("BIP0340/challenge", buf).toBigInteger().mod(Point.n)
        val R: Point? = Point.add(Point.mul(Point.G, s), Point.mul(P, Point.n.subtract(e)))

        !(R == null || !R.hasEvenY() || R.x?.compareTo(r) != 0)
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    fun ByteArray.toBigInteger() = BigInteger(1, this)

    fun taggedHash(tag: String, msg: ByteArray): ByteArray {
        val tagHash: ByteArray = sha256(tag.toByteArray())
        val len = tagHash.size * 2 + msg.size
        val buf = ByteArray(len)
        System.arraycopy(tagHash, 0, buf, 0, tagHash.size)
        System.arraycopy(tagHash, 0, buf, tagHash.size, tagHash.size)
        System.arraycopy(msg, 0, buf, tagHash.size * 2, msg.size)
        return sha256(buf)
    }

    private fun liftX(b: ByteArray): Point {
        val x: BigInteger = b.toBigInteger()
        if (x >= Point.p) {
            throw IllegalArgumentException("x is out of range")
        }
        val ySq = x.modPow(BigInteger.valueOf(3L), Point.p).add(BigInteger.valueOf(7L)).mod(Point.p)
        val y = ySq.modPow(Point.p.add(BigInteger.ONE).divide(BigInteger.valueOf(4L)), Point.p)
        return if (y.modPow(BigInteger.TWO, Point.p).compareTo(ySq) != 0) {
            throw IllegalArgumentException("invalid x coordinate")
        } else {
            Point(x, if (y.and(BigInteger.ONE).compareTo(BigInteger.ZERO) == 0) y else Point.p.subtract(y))
        }
    }

    private data class Point(
        val x: BigInteger?,
        val y: BigInteger?
    ) {
        companion object {
            val p = BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16)
            val n = BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141", 16)
            val G = Point(
                BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16),
                BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16)
            )
            private val INFINITY_POINT = Point(null, null)

            fun mul(point: Point, n: BigInteger): Point? {
                var P: Point? = point
                var R: Point? = null
                for (i in 0..255) {
                    if (n.shiftRight(i).and(BigInteger.ONE) > BigInteger.ZERO) {
                        R = add(R, P)
                    }
                    P = add(P, P)
                }
                return R
            }

            fun add(P1: Point?, P2: Point?): Point? {
                if (P1 != null && P2 != null && P1.isInfinite && P2.isInfinite) {
                    return INFINITY_POINT
                }
                if (P1 == null || P1.isInfinite) {
                    return P2
                }
                if (P2 == null || P2.isInfinite) {
                    return P1
                }
                if (P1.x == P2.x && P1.y != P2.y) {
                    return INFINITY_POINT
                }
                val lam: BigInteger = if (P1 == P2) {
                    val base = P2.y!!.multiply(BigInteger.TWO)
                    BigInteger.valueOf(3L).multiply(P1.x).multiply(P1.x).multiply(
                        base.modPow(
                            p.subtract(BigInteger.TWO),
                            p
                        )
                    ).mod(p)
                } else {
                    val base = P2.x!!.subtract(P1.x)
                    P2.y!!.subtract(P1.y).multiply(
                        base.modPow(
                            p.subtract(BigInteger.TWO),
                            p
                        )
                    ).mod(p)
                }
                val x3 = lam.multiply(lam).subtract(P1.x).subtract(P2.x).mod(p)
                return Point(x3, lam.multiply(P1.x!!.subtract(x3)).subtract(P1.y).mod(p))
            }

        }

        val isInfinite: Boolean = x == null || y == null

        fun hasEvenY(): Boolean {
            return y != null && y.mod(BigInteger.TWO).compareTo(BigInteger.ZERO) == 0
        }

    }


}