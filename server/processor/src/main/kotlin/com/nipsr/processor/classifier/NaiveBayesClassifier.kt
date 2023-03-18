package com.nipsr.processor.classifier

import java.util.*
import kotlin.math.ln

data class Document(val text: String, val isSpam: Boolean)

class NaiveBayesClassifier {
    private val spamWordCounts = mutableMapOf<String, Int>()
    private val hamWordCounts = mutableMapOf<String, Int>()
    private var totalSpamWords = 0
    private var totalHamWords = 0
    private var totalSpamDocs = 0
    private var totalHamDocs = 0

    fun train(documents: List<Document>) {
        for (doc in documents) {
            val words = tokenize(doc.text)
            if (doc.isSpam) {
                totalSpamDocs++
                totalSpamWords += words.size
            } else {
                totalHamDocs++
                totalHamWords += words.size
            }

            words.forEach { word ->
                if (doc.isSpam) {
                    spamWordCounts[word] = spamWordCounts.getOrDefault(word, 0) + 1
                } else {
                    hamWordCounts[word] = hamWordCounts.getOrDefault(word, 0) + 1
                }
            }
        }
    }

    fun predict(text: String): Boolean {
        val words = tokenize(text)
        val spamLogLikelihood = words.sumOf { ln(wordProbability(it, true)) }
        val hamLogLikelihood = words.sumOf { ln(wordProbability(it, false)) }

        val spamPrior = ln(totalSpamDocs.toDouble() / (totalSpamDocs + totalHamDocs))
        val hamPrior = ln(totalHamDocs.toDouble() / (totalSpamDocs + totalHamDocs))

        return spamPrior + spamLogLikelihood > hamPrior + hamLogLikelihood
    }

    private fun tokenize(text: String) = text.lowercase(Locale.getDefault()).split("\\s+".toRegex())

    private fun wordProbability(word: String, isSpam: Boolean): Double {
        val counts = if (isSpam) spamWordCounts else hamWordCounts
        val totalWords = if (isSpam) totalSpamWords else totalHamWords
        return (counts.getOrDefault(word, 0) + 1.0) / (totalWords + counts.size)
    }
}

fun main() {
    val trainingData = listOf(
        Document("Hey, check out these amazing shoes on sale!", true),
        Document("Can you help me with my homework?", false),
        Document("Win a free iPhone by clicking this link!", true),
        Document("How was your day today?", false),
        Document("Your account has been compromised, reset your password here!", true),
        Document("Do you want to grab some coffee later?", false),
        Document("Get a 50% discount on all our products now!", true),
        Document("Let's go to the movies this weekend!", false),
        Document("Congratulations! You've won a gift card!", true),
        Document("What's the best way to learn a new language?", false),
        Document("Amazing! Lose weight fast with this incredible new product!", true),
        Document("Did you finish the book I lent you?", false),
        Document("Upgrade your phone plan now and save money!", true),
        Document("Are you going to the party tonight?", false),
        Document("Special offer: buy one, get one free!", true),
        Document("What time should we meet tomorrow?", false),
        Document("Limited time offer: get a free trial of our premium service!", true),
        Document("Have you seen the new season of that show?", false),
        Document("Earn extra cash working from home!", true),
        Document("Let's schedule a meeting for next week.", false)
    )

    val classifier = NaiveBayesClassifier()
    classifier.train(trainingData)

    val testMessage = "Hey friend! Have a special day!"
    println("Is the message spam? ${classifier.predict(testMessage)}")
}
