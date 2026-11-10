package vn.edu.vlu.lab09

import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import vn.edu.vlu.lab09.databinding.ActivityMainBinding
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user == null) {
            openAsRoot(AuthActivity::class.java)
            return
        }

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fitSystemBars(binding.root)

        binding.tvEmail.text = user.email.orEmpty()
        Glide.with(this)
            .load(avatarUrl(user))
            .placeholder(R.drawable.ic_avatar)
            .error(R.drawable.ic_avatar)
            .circleCrop()
            .into(binding.ivAvatar)

        val columns = (resources.configuration.screenWidthDp / 240).coerceIn(1, 4)
        binding.rvPhotos.layoutManager = GridLayoutManager(this, columns)
        binding.rvPhotos.adapter = PhotoAdapter(PhotoCatalog.items)

        binding.toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.actionLogout) {
                auth.signOut()
                openAsRoot(AuthActivity::class.java)
                true
            } else {
                false
            }
        }
    }

    private fun avatarUrl(user: FirebaseUser): String {
        // Derive a stable seed without including the raw email or UID in the URL.
        val input = "${user.uid}:${user.email.orEmpty()}"
        val digest = MessageDigest.getInstance("SHA-256").digest(input.toByteArray(Charsets.UTF_8))
        val seed = Base64.encodeToString(digest, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
        return "https://api.dicebear.com/9.x/identicon/png?seed=$seed&size=128"
    }
}
