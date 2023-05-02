package ca.qc.cstj.tpsynthese.ui.ticket.detail

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.qc.cstj.tenretni.core.ColorHelper
import ca.qc.cstj.tpsynthese.R
import ca.qc.cstj.tpsynthese.databinding.ItemGatewaysBinding
import ca.qc.cstj.tpsynthese.domain.models.Gateway

class DetailTicketRecyclerViewAdapter (
    var gateways:List<Gateway> = listOf(),
    private val onGatewayClick:(Gateway)->Unit) : RecyclerView.Adapter<DetailTicketRecyclerViewAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, iewType: Int): ViewHolder {
        return ViewHolder(ItemGatewaysBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun onBindViewHolder(holder: DetailTicketRecyclerViewAdapter.ViewHolder, position: Int) {
        val gateway = gateways[position]
        holder.bind(gateway)

        holder.itemView.setOnClickListener {
            onGatewayClick(gateway)
        }
    }

    override fun getItemCount(): Int = gateways.size

    inner class ViewHolder(private val binding:ItemGatewaysBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(gateway:Gateway){
            with(binding){
                chipStatusDetail.text = gateway.connection.status
                chipStatusDetail.chipBackgroundColor = ColorHelper.connectionStatusColor(root.context, gateway.connection.status)
                if(gateway.connection.status == "Online"){
                    // TODO : Make a group with the txvPing downlaod and upload
                    txvPing.visibility = View.VISIBLE
                    txvDownload.visibility = View.VISIBLE
                    txvUpload.visibility = View.VISIBLE
                    txvNA.visibility = View.GONE
                    txvPing.text = root.context.getString(R.string.txvPing,gateway.connection.ping.toString())
                    txvDownload.text = root.context.getString(R.string.txvUploadDownload, gateway.connection.download.toString())
                    txvUpload.text = root.context.getString(R.string.txvUploadDownload, gateway.connection.upload.toString())
                }
                else{
                    txvPing.visibility = View.GONE
                    txvDownload.visibility = View.GONE
                    txvUpload.visibility = View.GONE
                    txvNA.visibility = View.VISIBLE
                }
                txvSerialNumber.text = gateway.serialNumber
            }
        }
    }
}