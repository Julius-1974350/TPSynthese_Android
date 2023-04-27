package ca.qc.cstj.tpsynthese.ui.ticket.list

import androidx.recyclerview.widget.RecyclerView
import ca.qc.cstj.tpsynthese.domain.models.Ticket
import android.view.LayoutInflater
import android.view.ViewGroup
import ca.qc.cstj.tpsynthese.R
import ca.qc.cstj.tpsynthese.databinding.ItemTicketBinding

class TicketRecyclerViewAdapter(
    var tickets: List<Ticket> = listOf(),
    private val onTicketClick: (Ticket) -> Unit) : RecyclerView.Adapter<TicketRecyclerViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
            return ViewHolder(ItemTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val ticket = tickets[position]
            holder.bind(ticket)

            holder.itemView.setOnClickListener {
                onTicketClick(ticket)
            }
        }

    override fun getItemCount() = tickets.size

    inner class ViewHolder(private val binding: ItemTicketBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(ticket: Ticket)  {
            with(binding) {
                txvTicketCode.text = root.context.getString(R.string.txvTicketCode, ticket.ticketNumber)
                txvTicketDate.text = ticket.createdDate
                chipPriority.text = ticket.priority
                chipStatus.text = ticket.status

                // TODO: Modifier la couleur des chips
            }

        }
    }


    }




