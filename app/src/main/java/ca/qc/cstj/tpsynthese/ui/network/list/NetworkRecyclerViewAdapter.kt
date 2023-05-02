package ca.qc.cstj.tpsynthese.ui.network.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.qc.cstj.tenretni.core.ColorHelper
import ca.qc.cstj.tpsynthese.R
import ca.qc.cstj.tpsynthese.databinding.ItemNetworkBinding
import ca.qc.cstj.tpsynthese.databinding.ItemTicketBinding
import ca.qc.cstj.tpsynthese.domain.models.Network
import ca.qc.cstj.tpsynthese.domain.models.NetworkNode
import ca.qc.cstj.tpsynthese.domain.models.Ticket

class NetworkRecyclerViewAdapter(var network: Network): RecyclerView.Adapter<NetworkRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NetworkRecyclerViewAdapter.ViewHolder {
        return ViewHolder(ItemNetworkBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: NetworkRecyclerViewAdapter.ViewHolder, position: Int) {
        val node = network.nodes[position]
        holder.bind(node)
    }

    override fun getItemCount() = network.nodes.size

    inner class ViewHolder(private val binding: ItemNetworkBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(node: NetworkNode)  {
            with(binding) {
                chipNetworkStatusDetail.text = node.connection.status
                chipNetworkStatusDetail.chipBackgroundColor = ColorHelper.connectionStatusColor(root.context, node.connection.status)
                txvIp.text = node.connection.ip
                txvName.text=node.name
                txvNetworkPing.text = root.context.getString(R.string.txvPing,node.connection.ping.toString())
                txvNetworkUpload.text=root.context.getString(R.string.txvUploadDownload,node.connection.upload.toString())
                txvNetworkDownload.text=root.context.getString(R.string.txvUploadDownload, node.connection.download.toString())
                txvNetworkSignal.text=root.context.getString(R.string.txvSignalQuality, node.connection.signal.toString())
            }
        }
    }
}