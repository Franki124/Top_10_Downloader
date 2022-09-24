package learnprogramming.academy

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.lang.Exception

class ParseApplications {
    val applications = ArrayList<FeedEntry>()

    fun parse(xmlData: String): Boolean {
        var status = true
        var inItem = false
        var textValue = ""

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var eventType = xpp.eventType
            var currentRecord = FeedEntry()
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = xpp.name?.lowercase()
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (tagName == "item") {
                            inItem = true
                        }
                    }

                    XmlPullParser.TEXT -> textValue = xpp.text

                    XmlPullParser.END_TAG -> {
                        if (inItem) {
                            when (tagName) {
                                "item" -> {
                                    applications.add(currentRecord)
                                    currentRecord = FeedEntry()     // create new object
                                }

                                "title" -> currentRecord.title = textValue
                                "link" -> currentRecord.link = textValue
                                "pubdate" -> currentRecord.pubDate = textValue
                                "category" -> currentRecord.category = textValue
                                "description" -> currentRecord.category = textValue
                            }
                        }
                    }
                }
                eventType = xpp.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            status = false
        }

        return false
    }
}