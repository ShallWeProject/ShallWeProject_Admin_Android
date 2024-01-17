import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.admin.R
import com.shall_we.admin.databinding.ItemTimeBinding
import com.shall_we.admin.reservation.data.TimeData

class TimeAdapter(
    private val timeList: List<TimeData>,
    private val onItemClicked: (TimeData) -> Unit
) : RecyclerView.Adapter<TimeAdapter.TimeListViewHolder>() {

    var selectedPosition = -1  // 선택된 아이템의 인덱스를 저장하는 변수

    inner class TimeListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemTimeBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_time, parent, false)
        return TimeListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeListViewHolder, position: Int) {
        val timeData = timeList[position]
        holder.binding.time.text = timeData.time

        holder.itemView.setOnClickListener {
            selectedPosition = position
            onItemClicked(timeData)
            notifyDataSetChanged()  // 데이터 변경을 알려줍니다.
        }

        // 선택된 아이템의 디자인을 변경합니다.
        if (selectedPosition == position) {
            holder.binding.root.setBackgroundResource(R.drawable.back_selected)  // 선택된 경우의 배경
        } else {
            holder.binding.root.setBackgroundResource(R.drawable.back)  // 선택되지 않은 경우의 배경
        }
    }

    override fun getItemCount() = timeList.size
}
