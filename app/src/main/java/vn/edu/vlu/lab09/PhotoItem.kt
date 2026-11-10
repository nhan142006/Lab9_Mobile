package vn.edu.vlu.lab09

data class PhotoItem(val id: Int, val title: String, val author: String, val imageUrl: String)

internal object PhotoCatalog {
    val items = listOf(
        photo(0, "Workspace"),
        photo(1, "At the desk"),
        photo(2, "A laptop and a notebook"),
        photo(3, "Work in progress"),
        photo(4, "Quiet desk"),
        photo(5, "A day at the computer"),
        photo(6, "Laptop study"),
        photo(7, "Coffee break"),
        photo(8, "Digital workspace"),
        photo(9, "An open laptop")
    )

    private fun photo(id: Int, title: String) =
        PhotoItem(id, title, "Alejandro Escamilla", "https://picsum.photos/id/$id/1200/800")
}
