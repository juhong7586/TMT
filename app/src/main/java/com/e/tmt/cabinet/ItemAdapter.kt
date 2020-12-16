package com.e.tmt.cabinet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.e.tmt.R
import com.e.tmt.memo.DetailFragment
import com.e.tmt.memo.Memo
import kotlinx.android.synthetic.main.fragment_dialog_custom.view.*
import kotlinx.android.synthetic.main.fragment_dialog_item_detail.view.*
import kotlinx.android.synthetic.main.item_recycler.view.*
import kotlinx.android.synthetic.main.stuff_recycler.view.*


class ItemAdapter(inputList: MutableList<Stuff>): RecyclerView.Adapter<ItemAdapter.ItemHolder>(),
    Filterable {
    var stuffList = mutableListOf<Stuff>()
    var selectedList = inputList
    var selecCabiList = mutableListOf<Stuff>()
    var selectedOne: String = ""
    var selectedNum = 0
    var beforeId: Int? = null
    var clearCheck: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ItemHolder {

        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.stuff_recycler, parent, false)

        return ItemHolder(itemView)
    }

    override fun getItemCount(): Int {
        return selectedList.size
    }
    override fun onBindViewHolder(holder: ItemAdapter.ItemHolder, position: Int) {
        val cell = selectedList[holder.adapterPosition]

        holder.setItems(cell)
    }


    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun setItems(item: Stuff){

                itemView.itemName.text = item.itemName
                itemView.checkBox4.isChecked = false

        }

        init {

            itemView.checkBox4.setOnCheckedChangeListener { v, isChecked ->
                //v.isChecked = itemView.itemName.text.toString() == beforeText && selectedList[adapterPosition].selected == 1

                if (isChecked) {
                    val currentId = itemView.id
                    val name = itemView.itemName.text.toString()
                    Toast.makeText(
                        itemView.context, "선택한 물건: $name",
                        Toast.LENGTH_SHORT
                    ).show()

                    selectedOne = name
                    selectedList[adapterPosition].selected = 1
                    beforeId = currentId
                }else{
                    selectedList[adapterPosition].selected = 0
                    v.isChecked = false
                }

            }

            itemView.itemName.setOnClickListener { v ->
                selectedNum = adapterPosition

                val theStuff = selectedList[selectedNum]

                val mDialogView = LayoutInflater.from(v!!.context).inflate(R.layout.fragment_dialog_item_detail, null)
                val mBuilder = AlertDialog.Builder(v.context)
                    .setView(mDialogView)
                val mAlertDialog = mBuilder.show()
                mDialogView.cabinetN.text =theStuff.cabinetName
                mDialogView.cellN.text=theStuff.cellName
                mDialogView.itemN.text=theStuff.itemName
                mDialogView.etcN.text=theStuff.etc
                mDialogView.whole_dialog.setOnClickListener { mAlertDialog.dismiss() }
            }
        }
    }

    override fun getFilter( ): Filter {
        return object: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults{

                val filterResults = FilterResults()

                    if(constraint == null || constraint.isEmpty()){
                        filterResults.values  =  stuffList
                        selecCabiList = stuffList
                    }else{
                        selecCabiList.clear()
                        val charSearch = constraint.toString().toLowerCase()
                        for (theStuff in 0 until stuffList.size){
                            val sstuff = stuffList[theStuff]

                            var theName = stuffList[theStuff].itemName
                            theName = theName.toString().toLowerCase()
                            if(theName.contains(charSearch.toString(), ignoreCase = true) ){
                                selecCabiList.add(sstuff)
                            }
                        }
                    }

                filterResults.values = selecCabiList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                selectedList = selecCabiList
                notifyDataSetChanged()
            }
        }
    }

}