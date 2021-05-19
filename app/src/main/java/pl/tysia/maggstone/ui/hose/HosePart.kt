package pl.tysia.maggstone.ui.hose

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_hose.*
import kotlinx.android.synthetic.main.component_hose_part.view.*
import kotlinx.android.synthetic.main.selected_person_button.view.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.constants.HosePartType.PART_CORD
import pl.tysia.maggstone.constants.HosePartType.PART_SLEEVE
import pl.tysia.maggstone.constants.HosePartType.PART_TIP1
import pl.tysia.maggstone.constants.HosePartType.PART_TIP2
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.ui.simple_list.SimpleListDialogFragment


class HosePart(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    var wasEdited = false
    private var _selected = false
    var type : String? = null

    val activity : AppCompatActivity = context as AppCompatActivity

    var viewModel: HoseViewModel? = null
    set(value) {
        field = value

        viewModel!!.hoseForm.observe(activity, {
            if (wasEdited) {
                return@observe
            }

            var error: Int? = null
            _selected = true

            when (type) {
                PART_CORD -> {
                    hosePart = viewModel!!.hose.cord
                 //   error = it.cordError
                }
                PART_TIP1 -> {
                    hosePart = viewModel!!.hose.tip1
                //    error = it.tip1Error
                }
                PART_TIP2 -> {
                    hosePart = viewModel!!.hose.tip2
                 //   error = it.tip2Error
                }
                PART_SLEEVE -> {
                    hosePart = viewModel!!.hose.sleeve
                //    error = it.sleeveError
                }
            }

            edit_text.error = if (error != null) activity.getString(error) else null

            setLoading(false)
        })
    }

    private var hosePart : Ware? = null
    set(value) {
        if (value != null){
            edit_text.setText(value!!.index!!)
            setHosePartCorrect(true)
            setEdited(false)
        }else{
            setHosePartCorrect(false)
            setEdited(false)
        }

        field = value
    }

    lateinit var title : String


    init {
        val inflater =
            activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        inflater.inflate(
            R.layout.component_hose_part,
            this
        )

        val a: TypedArray = getContext().obtainStyledAttributes(
            attrs,
            R.styleable.HosePart
        )

        type = a.getString(
            R.styleable.HosePart_partType
        )

        title = when(type){
            PART_CORD -> "Przewód"
            PART_TIP1 -> "Koncówka 1."
            PART_TIP2 -> "Końcówka 2."
            else -> "Tulejki"
        }

        a.recycle()

        edit_text.afterTextChanged {
            if (!_selected){
                setLoading(false)

                if (viewModel!!.hose.cord != null && type == PART_CORD){
                    viewModel!!.onCordChanged(null)
                }

                if (edit_text.text.isNullOrBlank()){
                    setHosePartCorrect(false)
                    setEdited(false)
                }else{
                    setHosePartCorrect(null)
                    setEdited(true)
                }
            }

            _selected = false
        }

        edit_text.hint = title

        search.setOnClickListener { onSearchClicked() }

        check.setOnClickListener {
            setLoading(true)
            wasEdited = false

            when(type) {
                PART_CORD -> {
                    viewModel?.onCordChanged(edit_text.text.toString())
                }
                PART_TIP1 -> {
                    viewModel?.onTip1Changed(edit_text.text.toString())
                }
                PART_TIP2 -> {
                    viewModel?.onTip2Changed(edit_text.text.toString())
                }
                PART_SLEEVE -> {
                    viewModel?.onSleeveChanged(edit_text.text.toString())
                }
            }
        }
    }

    fun onSearchClicked(){
        SimpleListDialogFragment.newInstance(title, edit_text.text.toString()).show(
            activity.supportFragmentManager,
            type
        )
    }

    fun setHosePartCorrect(correct: Boolean?){
        when (correct) {
            true -> {
                val check = activity.getDrawable(R.drawable.ic_check)
                edit_text.setCompoundDrawablesRelativeWithIntrinsicBounds(check, null, null, null)
                edit_text.setCompoundDrawables(check, null, null, null)
            }
            false -> {
                val wrong = activity.getDrawable(R.drawable.ic_round_close)
                edit_text.setCompoundDrawablesRelativeWithIntrinsicBounds(wrong, null, null, null)
                edit_text.setCompoundDrawables(wrong, null, null, null)
            }
            null -> {
                edit_text.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
                edit_text.setCompoundDrawables(null, null, null, null)
            }
        }
    }

    fun setEdited(edited: Boolean){
        wasEdited = edited

        when(edited) {
            true -> {
                check.visibility = View.VISIBLE
                check.setImageResource(R.drawable.ic_check)
            }
            false -> {
                check.visibility = View.GONE
            }
        }
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)

        if (visibility != VISIBLE){
            when(type) {
                PART_TIP1 -> {
                    viewModel?.onTip1Changed(null)
                }
                PART_TIP2 -> {
                    viewModel?.onTip2Changed(null)
                }
                PART_SLEEVE -> {
                    viewModel?.onSleeveChanged(null)
                }
            }
        }
    }

    fun setLoading(loading: Boolean){
        when(loading) {
            true -> {
                check.isClickable = false
                check.visibility = View.VISIBLE
                check.setImageResource(R.drawable.loading_animation)

                val animation = check.drawable as AnimationDrawable
                animation.start()
            }
            false -> {
                check.isClickable = true
                check.visibility = View.GONE

            }
        }
    }
}