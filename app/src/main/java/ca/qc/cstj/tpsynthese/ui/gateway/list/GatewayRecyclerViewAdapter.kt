package ca.qc.cstj.tpsynthese.ui.gateway.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.qc.cstj.tenretni.core.ColorHelper
import ca.qc.cstj.tpsynthese.R
import ca.qc.cstj.tpsynthese.databinding.ItemGatewaysBinding
import ca.qc.cstj.tpsynthese.domain.models.Gateway
import ca.qc.cstj.tpsynthese.domain.models.Ticket

class GatewayRecyclerViewAdapter(
    var gateways: List<Gateway> = listOf(),
    private val onGatewayClick: (Gateway) -> Unit) : RecyclerView.Adapter<GatewayRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        return ViewHolder(ItemGatewaysBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gateway = gateways[position]
        holder.bind(gateway)

        holder.itemView.setOnClickListener {
            onGatewayClick(gateway)
        }
    }

    override fun getItemCount() = gateways.size

    inner class ViewHolder(private val binding: ItemGatewaysBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(gateway: Gateway)  {
            with(binding) {
                chipStatusDetail.text = gateway.connection.status
                chipStatusDetail.chipBackgroundColor = ColorHelper.connectionStatusColor(root.context, gateway.connection.status)
                if(gateway.connection.status == "Online"){
                    // TODO : faire un groupe pour ping download et upload
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