package pl.tysia.maggstone.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment

private const val YES_NO_DIALOG = "pl.magg.maggroutes.yes_no_dialog"

/**
 *  Pokazuje nowe okienko dialogowe z przyciskami "Tak" i "Nie"
 *
 *  @param activity aktywność która wyświetla ten dialog
 *  @param title tytuł okienka dialogowego
 *  @param message wiadomość wyświetlona w okienku dialogowym
 *  @param onYes funkcja wywoływana po wciśnięciu przycisku "Tak",
 *  przyjmująca parametr [dialog] typu DialogFragment
 *  @param onNo funkcja wywoływana po wciśnięciu przycisku "Nie",
 *  przyjmująca parametr [dialog] typu DialogFragment
 *
 * */
fun showYesNoDialog(
    activity: AppCompatActivity, title: String, message: String,
    onYes: (dialog: DialogFragment) -> Unit,
    onNo: (dialog: DialogFragment) -> Unit
) {

    val dialog = YesNoDialogFragment.newInstance(title, message)

    dialog.listener = object : YesNoDialogFragment.YesNoListener {
        override fun onYesClicked(dialog: DialogFragment) {
            onYes(dialog)
        }

        override fun onNoClicked(dialog: DialogFragment) {
            onNo(dialog)
        }
    }
    dialog.show(activity.supportFragmentManager, YES_NO_DIALOG)
}



