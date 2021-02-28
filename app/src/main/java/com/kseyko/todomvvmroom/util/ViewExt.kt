package com.kseyko.todomvvmroom.util

import androidx.appcompat.widget.SearchView


/**     Code with ❤
╔════════════════════════════╗
║   Created by Seyfi ERCAN   ║
╠════════════════════════════╣
║  seyfiercan35@hotmail.com  ║
╠════════════════════════════╣
║      27,February,2021      ║
╚════════════════════════════╝
 */
inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit){
    this.setOnQueryTextListener(object  :SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })
}