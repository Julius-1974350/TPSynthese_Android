package ca.qc.cstj.tpsynthese.ui.gateway.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import ca.qc.cstj.tpsynthese.R
import ca.qc.cstj.tpsynthese.databinding.FragmentGatewayBinding
import ca.qc.cstj.tpsynthese.databinding.FragmentTicketBinding
import ca.qc.cstj.tpsynthese.domain.models.Gateway
import ca.qc.cstj.tpsynthese.domain.models.Ticket
import ca.qc.cstj.tpsynthese.ui.ticket.list.TicketFragmentDirections
import ca.qc.cstj.tpsynthese.ui.ticket.list.TicketRecyclerViewAdapter
import ca.qc.cstj.tpsynthese.ui.ticket.list.TicketUiState
import ca.qc.cstj.tpsynthese.ui.ticket.list.TicketViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class GatewayFragment : Fragment(R.layout.fragment_gateway) {

    private val binding: FragmentGatewayBinding by viewBinding()
    private val viewModel: GatewayViewModel by viewModels()

    private lateinit var gatewayRecyclerViewAdapter: GatewayRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gatewayRecyclerViewAdapter = GatewayRecyclerViewAdapter(listOf(), ::onRecyclerViewGatewayClick)

        binding.rcvGateways.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = gatewayRecyclerViewAdapter
        }

        viewModel.gatewayUIState.onEach {
            when (it) {
                is GatewayUIState.Error -> {
                    Toast.makeText(
                        requireContext(),
                        it.exception?.localizedMessage ?: getString(R.string.apiErrorMessage),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                GatewayUIState.Loading -> {
                    binding.rcvGateways.visibility = View.GONE
                }

                is GatewayUIState.Success -> {
                    binding.rcvGateways.visibility = View.VISIBLE
                    gatewayRecyclerViewAdapter.gateways = it.gateway
                    gatewayRecyclerViewAdapter.notifyDataSetChanged()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun onRecyclerViewGatewayClick(gateway: Gateway) {
        val action = GatewayFragmentDirections.actionNavigationGatewayToDetailGatewayFragment(gateway.href)
        findNavController().navigate(action)
    }
}