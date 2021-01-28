package pl.tysia.maggstone.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import pl.tysia.maggstone.R


private const val ARG_TITLE = "param1"
private const val ARG_MESSAGE = "param2"


class YesNoDialogFragment : DialogFragment() {
    private var title: String? = null
    private var message: String? = null

    var listener : YesNoListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments?.let {
            title = it.getString(ARG_TITLE)
            message = it.getString(ARG_MESSAGE)
        }

        return super.onCreateDialog(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_yes_no_dialog, container, false).also {
            it.findViewById<Button>(R.id.no_button).setOnClickListener {
                listener?.onNoClicked(this)
                dismiss()
            }
            it.findViewById<Button>(R.id.yes_button).setOnClickListener {
                listener?.onYesClicked(this)
                dismiss()
            }


            it.findViewById<TextView>(R.id.title_tv).text = title
            it.findViewById<TextView>(R.id.message_tv).text = message


        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String, message: String) =
            YesNoDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_MESSAGE, message)
                }
            }
    }

    interface YesNoListener{
        fun onYesClicked(dialog : DialogFragment)

        fun onNoClicked(dialog : DialogFragment)
    }
}