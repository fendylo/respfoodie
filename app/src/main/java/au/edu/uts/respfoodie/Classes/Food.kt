package au.edu.uts.respfoodie.Classes

class Food (val id: String,  var name: String, var description: String, var image_url: String){

    val ingredients = ArrayList<HashMap<String, String>>()
    val procedures = ArrayList<String>()
    var favourite = true

    fun addIngredients(name: String, quantity: String, quantifier: String){
        val ingredient = HashMap<String,String>()
        ingredient["name"] = name
        ingredient["quantity"] = quantity
        ingredient["quantifier"] = quantifier
        ingredients.add(ingredient)
    }

    fun addProcedure(step: String){
        procedures.add(step)
    }

    fun setFavorite(like: Boolean){
        favourite = like
    }
}