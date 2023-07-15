package ir.duniijet.api1.mainScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.duniijet.api1.databinding.ItemMainBinding
import ir.duniijet.api1.model.Student


class StudentAdapter(private val data:ArrayList<Student>, private val studentEvent: StudentEvent) :RecyclerView.Adapter<StudentAdapter.StudentVH>(){
    lateinit var binding: ItemMainBinding


    inner class StudentVH(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindViews(student: Student) {

            binding.txtName.text = student.name
            binding.txtCourse.text = student.course
            binding.txtScore.text = student.score.toString()
            binding.txtHarfAval.text = student.name[0].uppercaseChar().toString()

            itemView.setOnClickListener {
                studentEvent.onItemClicked(student, adapterPosition)
            }

            itemView.setOnLongClickListener {
               studentEvent.onItemLongClicked(student, adapterPosition)
                true
            }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentVH {
        binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentVH(binding.root)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: StudentVH, position: Int) {
        holder.bindViews(data[position])

    }
    fun removeItem(student: Student, position: Int) {

        data.remove(student)
        notifyItemRemoved(position)

    }
    fun addItem(student: Student){
        data.add(0,student)
        notifyItemInserted(0)
    }
    fun updateItem(student: Student, position: Int){
        data.set(position,student)
        notifyItemChanged(position)
    }
    interface StudentEvent {

        fun onItemClicked(student: Student, position: Int)
        fun onItemLongClicked(student: Student, position: Int)

    }
}