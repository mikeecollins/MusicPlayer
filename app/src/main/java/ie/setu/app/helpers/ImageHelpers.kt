package ie.setu.app.helpers

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import ie.setu.musicplayer.R


fun showImagePicker(context: Context, intentLauncher: ActivityResultLauncher<Intent>, titleResId: Int) {
    val chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        type = "image/*"
        addCategory(Intent.CATEGORY_OPENABLE)
    }
    val chooser = Intent.createChooser(chooseFile, context.getString(titleResId))
    intentLauncher.launch(chooser)
}




