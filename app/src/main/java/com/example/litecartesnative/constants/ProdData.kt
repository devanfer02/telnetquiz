package com.example.litecartesnative.constants

import com.example.litecartesnative.R
import com.example.litecartesnative.features.pretest.domain.model.Pretest
import com.example.litecartesnative.features.quiz.domain.model.Chapter
import com.example.litecartesnative.features.quiz.domain.model.Material
import com.example.litecartesnative.features.quiz.domain.model.Question

val pretestsData = mutableListOf(
    Pretest(
        question = "Manakah yang berbentuk seperti roda?",
        options = mutableListOf(
            "Segitiga",
            "Persegi",
            "Lingkaran"
        ),
        answer = "Lingkaran",
        imageId = R.drawable.a1_1a___3_bangun_datar
    ),
    Pretest(
        question = "Di antara benda tersebut, manakah yang paling panjang?",
        options = mutableListOf(
            "Pensil",
            "Penggaris",
            "Penghapus"
        ),
        answer = "Penggaris",
        imageId = R.drawable.p2___penghapus_pensil_penggaris
    ),
    Pretest(
        question = "Sebutkan ciri-ciri bangun datar ini:\n",
        options = mutableListOf(
            "Memiliki 4 sisi sama panjang",
            "Memiliki 2 pasang sisi sejajar dan sama panjang",
            "Memiliki 3 sisi sama panjang"
        ),
        answer = "Memiliki 2 pasang sisi sejajar dan sama panjang",
        imageId = R.drawable.b1_1___persegi_panjang
    ),
    Pretest(
        question = "Manakah posisi buku yang benar",
        options = mutableListOf(
            "Di samping meja",
            "Di bawah meja",
            "Di atas meja"
        ),
        answer = "Di atas meja",
        imageId = R.drawable.b2_1___meja_dan_buku
    )
)

val chaptersData = mutableListOf(
    Chapter(
        title = "Mengenal Bangun Datar",
        description = "Pada chapter ini, kita akan mengenal berbagai macam bangun datar",
        imageLink = R.drawable.chap1,
        levels = mutableListOf(
            mutableListOf(
                Question(
                    title = "Geometri",
                    imageId = R.drawable.fase_a_l1_1,
                    question = "Manakah yang berbentuk seperti roda?",
                    options = mutableListOf(
                        "Segitiga",
                        "Persegi",
                        "Lingkaran"
                    ),
                    description = "",
                    answer = "Lingkaran",
                    material = Material(
                        image = R.drawable.a1_1b___roda_dan_lingkaran,
                        title = "Mendeskripsikan benda berdasarkan bentuk",
                        description = "Lingkaran adalah bentuk yang bulat sempurna, seperti roda, koin, atau piring. Semua titik di tepi lingkaran memiliki jarak yang sama ke pusatnya."
                    )
                ),
            ),
            mutableListOf(
                Question(
                    title = "Geometri",
                    imageId = R.drawable.a2_1,
                    question = "Apakah Pohon A lebih tinggi dari pohon B?",
                    options = mutableListOf(
                        "Ya",
                        "Tidak",
                    ),
                    description = "Ayo kita amati gambar pohon pisang berikut.",
                    answer = "Ya",
                    material = Material(
                        title = "Membandingkan dan Mengurutkan Panjang Benda",
                        image = R.drawable.a2_1,
                        description = "Pohon A lebih tinggi dari Pohon B\nPohon B lebih pendek dari Pohon A\nKita menggunakan kata lebih tinggi dan lebih pendek untuk membandingkan tinggi 2 benda"
                    )
                ),
                Question(
                    title = "Geometri",
                    imageId = R.drawable.a2_2,
                    question = "Pilihlah pernyataan yang tepat",
                    options = mutableListOf(
                        "Pohon B lebih tinggi dari pohon C",
                        "Pohon B lebih pendek dari pohon C",
                        "Pohon B sama tinggi dengan pohon C"
                    ),
                    description = "Ayo kita bandingkan tinggi pohon pisang B dan C.",
                    answer = "Pohon B sama tinggi dengan pohon C",
                    material = Material(
                        title = "Membandingkan dan Mengurutkan Panjang Benda",
                        image = R.drawable.a2_2,
                        description = "2 Pohon tersebut memiliki tinggi yang sama.\nKita menggunakan kata sama tinggi untuk membandingkannya"
                    )
                ),
                Question(
                    title = "Geometri",
                    imageId = R.drawable.a2_3,
                    question = "Urutkan 4 pohon pada gambar dari yang paling tinggi hingga yang paling pendek",
                    options = mutableListOf(
                        "Urutan pohon dari yang paling tinggi adalah A, B, C, dan D atau A, C, B, dan D.",
                        "Urutan pohon dari yang paling tinggi adalah D, B, C, A atau D, C, B, A."
                    ),
                    description = "",
                    answer = "Urutan pohon dari yang paling tinggi adalah A, B, C, dan D atau A, C, B, dan D.",
                    material = Material(
                        title = "Membandingkan dan Mengurutkan Panjang Benda",
                        image = R.drawable.a2_3,
                        description = "Urutan pohon dari yang paling tinggi adalah A, B, C, dan D atau A, C, B, dan D.\nKarena Pohon A lebih tinggi dari pohon A dan pohon B, dan pohon B dan pohon C lebih tinggi dari pohon D, maka:\nUrutan pohon dari yang paling tinggi adalah A, B, C, dan D atau A, C, B, dan D."
                    )
                ),
                Question(
                    title = "Geometri",
                    imageId = R.drawable.a2_4a,
                    question = "Manakah yang lebih panjang?",
                    options = mutableListOf(
                        "Meja",
                        "Pensil"
                    ),
                    description = "Perhatikan jengkal tangan yang ada di gambar.",
                    answer = "Meja",
                    material = Material(
                        title = "Membandingkan dan Mengurutkan Panjang Benda",
                        image = R.drawable.a2_4b,
                        description = "Kita bisa mengukur panjang benda menggunakan satuan tidak baku seperti jengkal tangan, langkah kaki, atau pensil. Jengkal adalah jarak dari ujung ibu jari ke ujung jari kelingking saat tangan dibentangkan."
                    )
                ),
                Question(
                    title = "Geometri",
                    imageId = R.drawable.a2_4a,
                    question = "Berapakah panjang meja dan pensil dalam satuan jengkal tangan?",
                    options = mutableListOf(
                        "Panjang meja adalah 4 jengkal tangan; panjang pensil adalah 2 jengkal tangan.",
                        "Panjang meja adalah 5 jengkal tangan; panjang pensil adalah 1 jengkal tangan",
                        "Panjang meja adalah 2 jengkal tangan; panjang pensil adalah 1 jengkal tangan."
                    ),
                    description = "",
                    answer = "Panjang meja adalah 5 jengkal tangan; panjang pensil adalah 1 jengkal tangan",
                    material = Material(
                        title = "Mengukur Panjang Benda",
                        image = R.drawable.a2_4b,
                        description = "Jengkal adalah jarak dari ujung ibu jari ke ujung jari kelingking saat tangan dibentangkan.\n" + "Panjang pensil dan meja berbeda.\n" +
                                "Hasil pengukuran pensil dan meja berbeda.\n"
                    )
                )
            )
        )
    ),
    Chapter(
        title = "Mengenal Bangun Ruang",
        description = "Pada chapter ini, kita akan mengenal berbagai macam bangun ruang",
        imageLink = R.drawable.chap2,
        levels = mutableListOf(
            mutableListOf(
                Question(
                    title = "Geometri",
                    description = "Perhatikan persegi panjang ini!",
                    imageId = R.drawable.b1_1___persegi_panjang,
                    question = "Sebutkan ciri-ciri bangun datar ini",
                    options = mutableListOf(
                        "Memiliki 4 sisi sama panjang",
                        "Memiliki 2 pasang sisi sejajar dan sama panjang",
                        "Memiliki 3 sisi sama panjang"
                    ),
                    answer = "Memiliki 2 pasang sisi sejajar dan sama panjang",
                    material = Material(
                        title = "Menjelaskan ciri-ciri bangun datar",
                        image = R.drawable.b1_1___persegi_panjang,
                        description = "Persegi panjang memiliki 4 sudut siku-siku dan 2 pasang sisi yang sejajar dan sama panjang. Sisi yang berhadapan selalu sama panjang."
                    )
                ),
                Question(
                    title = "Geometri",
                    description = "Bangun datar ini bisa dibagi menjadi dua segitiga yang sama. ",
                    question = "Bangun apakah itu?",
                    options = mutableListOf(
                        "Persegi panjang",
                        "Persegi",
                        "Jajar genjang"
                    ),
                    answer = "Persegi",
                    material = Material(
                        title = "Mengurai satu bangun datar menjadi beberapa bangun datar lain.",
                        description = "Persegi dapat dibagi (didekomposisi) menjadi dua segitiga sama besar dengan menarik garis diagonal. Persegi adalah bangun datar yang memiliki empat sisi sama panjang dan empat sudut siku-siku."
                    )
                )
            ),
            mutableListOf(
                Question(
                    title = "Geometri",
                    description = "",
                    question = "Manakah posisi buku yang benar",
                    imageId = R.drawable.b2_1___meja_dan_buku,
                    options = mutableListOf(
                        "Di samping meja",
                        "Di bawah meja",
                        "Di atas meja",
                    ),
                    answer = "Di atas meja",
                    material = Material(
                        title = "Mendeskripsikan posisi benda terhadap benda lain",
                        image = R.drawable.b2_1___meja_dan_buku,
                        description = "Posisi benda dapat dijelaskan menggunakan kata: depan, belakang, kanan, atau kiri. Jika sebuah benda berada di hadapan kita, maka benda itu ada di depan. Jika benda berada di sisi tangan kanan kita, maka benda itu ada di kanan. Jika berada di tingkat di atas benda lain, maka benda itu ada di atas."
                    )
                )
            )
        )
    ),
    Chapter(
        title = "Mencirikan Bangun Datar",
        description = "Pada chapter ini, kita akan belajar ciri-ciri bangun datar dan bangun ruang",
        imageLink = R.drawable.chap2,
        levels = mutableListOf(
            mutableListOf(
                Question(
                    title = "Geometri",
                    imageId = R.drawable.fase_b_l1_1,
                    question = "Manakah yang merupakan ciri persegi panjang?\n",
                    options = mutableListOf(
                        "Semua sisi sama panjang",
                        "Memiliki dua pasang sisi sejajar dan sama panjang",
                        "Memiliki tiga sisi sama panjang"
                    ),
                    description = "Perhatikan persegi panjang pada gambar tersebut",
                    answer = "Semua sisi sama panjang"
                ),
                Question(
                    title = "Geometri",
                    imageId = R.drawable.fase_b_l1_2,
                    question = "Bangun apakah ini",
                    options = mutableListOf(
                        "Persegi panjang",
                        "Persegi",
                        "Jajar genjang"
                    ),
                    description = "Bangun datar ini bisa dibagi menjadi dua segitiga yang sama. ",
                    answer = "Persegi"
                )
            ),
            mutableListOf(
                Question(
                    title = "Pengukuran",
                    question = "Berapa meter panjang pita tersebut?",
                    options = mutableListOf(
                        "3 meter",
                        "30 meter",
                        "0.3 meter"
                    ),
                    description = "Budi memiliki sebuah pita yang memiliki panjang 300 centimeter",
                    answer = "3 meter"
                ),
                Question(
                    title = "Pengukuran",
                    imageId = R.drawable.fase_b_l2_2,
                    question = "Berapa luas persegi pada gambar tersebut jika dihitung menggunakan satuan persegi?",
                    options = mutableListOf(
                        "8 satuan persegi",
                        "12 satuan persegi",
                        "16 satuan persegi"
                    ),
                    description = "Perhatikan gambar tersebut",
                    answer = "16 satuan persegi"
                )
            )
        )
    ),
)