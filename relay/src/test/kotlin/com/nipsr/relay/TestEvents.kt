package com.nipsr.relay

import com.nipsr.payload.ObjectMapperUtils.mapTo
import com.nipsr.payload.ObjectMapperUtils.objectMapper
import com.nipsr.payload.factory.toEvent
import com.nipsr.payload.model.inputs.EventInput

object TestEvents {

    private val eventClass = EventInput::class.java

    private val invalidEventString = """
        {
      "kind": 1,
      "content": "invalid",
      "tags": [
        
      ],
      "pubkey": "93e68814cd5d4dc3e920703f2673411b3980810ab063b6c4536d0aa405a3e3c9",
      "created_at": 1677687496,
      "id": "169e33ca2c8b9c9084f91aa967398bc8d376c57d3dd1e5e073b0ac702df60fa1",
      "sig": "7e3d4dc10627a37f298924b83e7c95decf87184db5e92e0aa5bcf12b5fe56d93f692538241d0fba41356a2f7801ebbb63ccc400184c9e2236c903d49d690fba5"
    }
    """.trimIndent()

    val invalidEvent = objectMapper.readValue(invalidEventString, eventClass).toEvent()

    val validEvents = arrayListOf(
        """
            {
              "kind": 1,
              "content": "test01",
              "tags": [
                
              ],
              "pubkey": "93e68814cd5d4dc3e920703f2673411b3980810ab063b6c4536d0aa405a3e3c9",
              "created_at": 1677687669,
              "id": "4b176814c40d96fda3afd6e5519fe41983da87e518242c40b3845df296fbfd9f",
              "sig": "5c5d10a614f2e08a65d923c1eeb3dba1b02ef551fb5ec27a421587fcfbd8db5fd6c387845c918739ad09e00c5c9223aafd118cdf3d1fe85decf421ad4c38de1d"
            }
        """.trimIndent(),
        """
            {
              "kind": 1,
              "content": "#[0] test02",
              "tags": [
                [
                  "p",
                  "13409737c571a3fc923faa407935287e440b1d5a1f64a75c87bed37a4f1b74ab",
                  "wss://brb.io",
                  "juye"
                ]
              ],
              "pubkey": "93e68814cd5d4dc3e920703f2673411b3980810ab063b6c4536d0aa405a3e3c9",
              "created_at": 1677687728,
              "id": "5a665235653be85a878770b56e8cd21ed1079e5b43e3a4c8e5e13e7d5506e1f1",
              "sig": "dc89b64b4706754a7cc14b994c0e7919d9d4ccc23d45683e274adbd6e74e9e077ebbbd5af80d42de0bc942efa4aaf7ed1f9b649c3e019d884329b28d08b9e16d"
            }
        """.trimIndent(),
        """
            {
              "kind": 1,
              "content": "#test #03",
              "tags": [
                [
                  "t",
                  "#test"
                ],
                [
                  "t",
                  "#03"
                ]
              ],
              "pubkey": "93e68814cd5d4dc3e920703f2673411b3980810ab063b6c4536d0aa405a3e3c9",
              "created_at": 1677687626,
              "id": "388b7a192122f198219a11482ce79195ca44ff2890d8352fda64ebbdd7753b7e",
              "sig": "a57dde5b8bc9f490b343bbc5cd9f35dc6b517fa0c76692ab14350d6125aecb40223032fb694d7ecc0af2d6f1828374c797b78937db9e8836cf5c05cca4e20a72"
            }
        """.trimIndent()
    ).map {
        it.mapTo(eventClass).toEvent()
    }

}