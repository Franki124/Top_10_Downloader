package learnprogramming.academy

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ArrayAdapter
import android.widget.TextView

class ViewHolder(v: View) {
    val tvName: TextView = v.findViewById(R.id.tvName)
    val tvArtist: TextView = v.findViewById(R.id.tvArtist)
    val tvSummary: TextView = v.findViewById(R.id.tvSummary)
}

class FeedAdapter(
    context: Context,
    private val resource: Int,
    private val applications: List<FeedEntry>
) : ArrayAdapter<FeedEntry>(context, resource) {

    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return applications.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            view = inflater.inflate(resource, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val currentApp = applications[position]

        viewHolder.tvName.text = currentApp.title
        viewHolder.tvArtist.text = currentApp.category
        viewHolder.tvSummary.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(currentApp.description, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(currentApp.description.toString())
        }

        return view
    }
}
