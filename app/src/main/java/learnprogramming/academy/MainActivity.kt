package learnprogramming.academy

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import java.net.URL
import kotlin.properties.Delegates

class FeedEntry {
    var title: String = ""
    var link: String = ""
    var pubDate: String = ""
    var category: String = ""
    var description: String = ""

    override fun toString(): String {
        return """
            title = $title
            link = $link
            pubDate = $pubDate
            category = $category
            description = $description
        """.trimIndent()
    }
}

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private var downloadData: DownloadData? = null

    private var feedCachedUrl = "INVALIDATED"
    private val STATE_URL = "feedUrl"
    private var feedUrl: String = "https://www.gry-online.pl/rss/news.xml"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            feedUrl = savedInstanceState.getString(STATE_URL)!!
        }

        downloadUrl(feedUrl)
    }

    private fun downloadUrl(feedUrl: String) {
        if (feedUrl != feedCachedUrl) {
            downloadData = DownloadData(this, findViewById(R.id.xmlListView))
            downloadData?.execute(feedUrl)
            feedCachedUrl = feedUrl
        } else {
            Log.d(TAG, "downloadUrl - URL not changed")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.feeds_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.mnuFree ->
                feedUrl = "https://www.gry-online.pl/rss/news.xml"
            R.id.mnuTechnology ->
                feedUrl = "https://www.gry-online.pl/rss/tech.xml"
            R.id.mnuMoviesShows ->
                feedUrl = "https://www.gry-online.pl/rss/filmy.xml"
            R.id.mnuRefresh -> feedCachedUrl = "INVALIDATE"
            else ->
                return super.onOptionsItemSelected(item)
        }
        downloadUrl(feedUrl)
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_URL, feedUrl)
    }

    override fun onDestroy() {
        super.onDestroy()
        downloadData?.cancel(true)
    }

    companion object {
        private class DownloadData(context: Context, listView: ListView) : AsyncTask<String, Void, String>() {
            private val TAG = "DownloadData"

            var propContext : Context by Delegates.notNull()
            var propListView : ListView  by Delegates.notNull()

            init {
                propContext = context
                propListView = listView
            }

            override fun doInBackground(vararg url: String?): String {
                val rssFeed = downloadXML(url[0])
                if (rssFeed.isEmpty()) {
                    Log.e(TAG, "doInBackground: Error downloading")
                }
                return rssFeed
            }

            override fun onPostExecute(result: String) {
                super.onPostExecute(result)
                val parseApplications = ParseApplications()
                parseApplications.parse(result)

                val feedAdapter = FeedAdapter(propContext, R.layout.list_record, parseApplications.applications)
                propListView.adapter = feedAdapter
            }

            private fun downloadXML(urlPath: String?): String {
                return URL(urlPath).readText()
            }
        }
    }
}
