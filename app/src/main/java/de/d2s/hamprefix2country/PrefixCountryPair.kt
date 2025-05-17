package de.d2s.hamprefix2country

class PrefixCountryPair(prefixIn: String, countryIn: String) {
    val prefix: String = prefixIn
    val country: String = countryIn
}

class PrefixCountryCSV(fileString: String) {
    private val fileText = fileString
    private var prefixCountryList: ArrayList<PrefixCountryPair> = arrayListOf<PrefixCountryPair>()

    fun readCsv() {
        // get lines
        val lines: List<String> = fileText.split("\n")
        for (line in lines) {
            val pair = line.split(";").toTypedArray()
            if (pair.size == 2) {
                prefixCountryList.add(PrefixCountryPair(pair[0], pair[1]))
            }
        }
    }

    fun findCountriesForPrefix(prefixIn: String): ArrayList<String> {
        val outList: ArrayList<String> = arrayListOf<String>()
        // Brute-force search through all prefixes
        for (pcp in prefixCountryList) {
            if (pcp.prefix.startsWith(prefixIn)) {
                outList.add(pcp.country)
            }
            // add exact match at beginning
            if (pcp.prefix == prefixIn) {
                outList.add(0, pcp.country)
            }
        }
        // remove duplicates on return
        return ArrayList(outList.distinct())
    }
}
