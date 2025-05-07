package ie.setu.app.helpers

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import ie.setu.musicplayer.R


fun showImagePicker(context: Context, intentLauncher: ActivityResultLauncher<Intent>, titleResId: Int) {
    val chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        type = "image/*"
        addCategory(Intent.CATEGORY_OPENABLE)
    }
    val chooser = Intent.createChooser(chooseFile, context.getString(titleResId))
    intentLauncher.launch(chooser)
}




