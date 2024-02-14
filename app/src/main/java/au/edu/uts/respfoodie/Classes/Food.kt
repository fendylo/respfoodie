package au.edu.uts.respfoodie.Classes

import android.os.Parcel
import android.os.Parcelable

class Food(
    val id: String,
    var name: String,
    var description: String,
    var image_url: String,
    var ingredients: ArrayList<HashMap<String, String>> = ArrayList(),
    var procedures: ArrayList<String> = ArrayList(),
    var favourite: Boolean = false
) : Parcelable {

    fun addIngredients(name: String, quantity: String) {
        val ingredient = HashMap<String, String>()
        ingredient["name"] = name
        ingredient["quantity"] = quantity
        ingredients.add(ingredient)
    }

    fun addProcedure(step: String) {
        procedures.add(step)
    }

    fun setFavorite(like: Boolean) {
        favourite = like
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
        favourite = parcel.readByte() != 0.toByte()

        val ingredientsList: ArrayList<HashMap<String, String>> = arrayListOf()
        parcel.readList(ingredientsList as List<*>, HashMap::class.java.classLoader)
        ingredients = ingredientsList

        val proceduresList: ArrayList<String> = arrayListOf()
        parcel.readList(proceduresList as List<*>, String::class.java.classLoader)
        procedures = proceduresList
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(image_url)
        parcel.writeByte(if (favourite) 1 else 0)
        parcel.writeList(ingredients as List<*>)
        parcel.writeList(procedures as List<*>)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Food> {
        override fun createFromParcel(parcel: Parcel): Food {
            return Food(parcel)
        }

        override fun newArray(size: Int): Array<Food?> {
            return arrayOfNulls(size)
        }
    }
}
