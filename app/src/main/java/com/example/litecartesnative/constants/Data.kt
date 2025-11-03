package com.example.litecartesnative.constants

import com.example.litecartesnative.features.quiz.domain.model.Question
import com.example.litecartesnative.features.user.domain.model.User

val questionDummy = Question(
    title = "Artificial Intelligence (AI)",
    description = "Artificial Intelligence (AI) menjadi semakin terdepan dalam dunia teknologi. Kemampuannya untuk mengenali pola, belajar dari pengalaman, dan membuat keputusan semakin kompleks, memperluas penggunaannya dari industri hingga ke kehidupan sehari-hari. Dari asisten virtual yang bisa merespon pertanyaan kita hingga mobil otonom yang dapat mengemudi sendiri, kehadiran AI telah mengubah cara kita berinteraksi dengan teknologi.",
    question = "Apa yang membuat Artificial Intelligence semakin dominan dalam dunia teknologi?",
    options = mutableListOf(
        "Kemampuan belajar dari pengalaman",
        "Keterbatasan dalam penggunaan",
        "Ketergantungan pada manusia",
        "Kelemahan dalam mengenali pola"
    ),
    answer = "Kelemahan dalam mengenali pola"
)

val usersDummy = mutableListOf(
    User(
        fullname = "Emily Johnson",
        handle = "@EmiTheArtist",
        exp = 2
    ),
    User(
        fullname = "Liam Carter",
        handle = "@LiamLegoMaster",
        exp = 3
    ),
    User(
        fullname = "Sophia Nguyen",
        handle = "@SophiStarReader",
        exp = 4
    ),
    User(
        fullname = "Jackson White",
        handle = "@JackWizKid",
        exp = 3
    ),
    User(
        fullname = "Isabella Martinez",
        handle = "@BellaBaker",
        exp = 2
    ),
    User(
        fullname = "Noah Robinson",
        handle = "@NoahSoccerPro",
        exp = 5
    ),
    User(
        fullname = "Olivia King",
        handle = "@OliviaOrigami",
        exp = 3
    ),
    User(
        fullname = "Mason Brooks",
        handle = "@MasonTechTinkerer",
        exp = 4
    ),
    User(
        fullname = "Mia Patel",
        handle = "@MiaMusicalNotes",
        exp = 5
    ),
    User(
        fullname = "Ethan Garcia",
        handle = "@EthanSkateboard",
        exp = 3
    )
)