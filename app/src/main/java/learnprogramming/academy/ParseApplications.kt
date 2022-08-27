package learnprogramming.academy

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.lang.Exception

class ParseApplications {
    private val TAG = "ParseApplications"
    val applications = ArrayList<FeedEntry>()

    fun parse(xmlData: String): Boolean {
        Log.d(TAG, "parse called with $xmlData")
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
                val tagName = xpp.name.lowercase()          //TODO: we should use the safe-call operator ?
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        Log.d(TAG, "parse: Starting tag for " + tagName)
                        if (tagName == "item") {
                            inItem = true
                        }
                    }

                    XmlPullParser.TEXT -> textValue = xpp.text

                    XmlPullParser.END_TAG -> {
                        Log.d(TAG, "parse: Ending tag for " + tagName)
                        if (inItem) {
                            when (tagName) {
                                "item" -> {
                                    applications.add(currentRecord)
                                    currentRecord = FeedEntry()     // create new object
                                }

                                "title" -> currentRecord.title = textValue
                                "link" -> currentRecord.link = textValue
                                "pubDate" -> currentRecord.pubDate = textValue
                                "category" -> currentRecord.category = textValue
                                "guid" -> currentRecord.guid = textValue
                            }
                        }
                    }
                }

                // Nothing else to do.
                eventType = xpp.next()
            }

            for (app in applications) {
                Log.d(TAG,"*************")
                Log.d(TAG, app.toString())
            }

        } catch (e: Exception) {
            e.printStackTrace()
            status = false
        }

        return false
    }
}