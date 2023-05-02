package ca.qc.cstj.tpsynthese.ui.network.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import ca.qc.cstj.tpsynthese.R
import ca.qc.cstj.tpsynthese.databinding.FragmentNetworkBinding
import ca.qc.cstj.tpsynthese.domain.models.Network
import ca.qc.cstj.tpsynthese.domain.models.Ticket
import ca.qc.cstj.tpsynthese.ui.ticket.list.TicketFragmentDirections
import ca.qc.cstj.tpsynthese.ui.ticket.list.TicketRecyclerViewAdapter
import ca.qc.cstj.tpsynthese.ui.ticket.list.TicketUiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NetworkFragment : Fragment(R.layout.fragment_network) {

    private val binding:FragmentNetworkBinding by viewBinding()
    private val viewModel: NetworkViewModel by viewModels()
    private lateinit var networkRecyclerViewAdapter: NetworkRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onResume()
    }
    override fun onResume() {
        super.onResume()
        showNetworks()
    }
    @SuppressLint("SetTextI18n")
    private fun showNetworks(){
        networkRecyclerViewAdapter = NetworkRecyclerViewAdapter(Network())

        binding.rcvNetworks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = networkRecyclerViewAdapter
        }

        viewModel.networkUiState.onEach {
            when(it) {
                is ListNetworkUIState.Error -> {
                    Toast.makeText(requireContext(), it.exception?.localizedMessage ?: getString(R.string.apiErrorMessage), Toast.LENGTH_SHORT).show()
                }
                ListNetworkUIState.Loading -> {
                    binding.rcvNetworks.visibility = View.GONE
                }
                is ListNetworkUIState.Success -> {
                    // TODO : date format
                    binding.txvLstUpt.text = "Last Update: ${it.network.updateDate.format()}"
                    binding.txvNxtReboot.text = "Next Reboot at: ${it.network.nextReboot}"
                    binding.rcvNetworks.visibility = View.VISIBLE
                    networkRecyclerViewAdapter.network = it.network
                    networkRecyclerViewAdapter.notifyDataSetChanged()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }
}