package com.decorator1889.instruments.util.enums

import com.decorator1889.instruments.R
import com.decorator1889.instruments.util.str

enum class Types(val types: String) {
    SURGERY(str(R.string.surgery)),
    STOMATOLOGY(str(R.string.stomatology)),
    GYNECOLOGY(str(R.string.gynecology)),
    NEURO(str(R.string.neuro)),
    LOR(str(R.string.lor)),
    UROLOGY(str(R.string.urology)),
    OPHTHALMOLOGY(str(R.string.ophthalmology)),
    ANESTHESIOLOGY(str(R.string.anesthesiology))
}