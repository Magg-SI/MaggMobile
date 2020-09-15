package pl.tysia.maggstone.ui.document

import android.os.Bundle
import pl.tysia.maggstone.R

class BasicNewDocumentActivity : NewDocumentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_new_document)

        super.onCreate(savedInstanceState)
    }
}