package ca.qc.cstj.tpsynthese.ui.ticket.list

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import ca.qc.cstj.tpsynthese.databinding.FragmentTicketBinding
import android.viewbinding.library.fragment.viewBinding
import androidx.navigation.fragment.findNavController
import ca.qc.cstj.tpsynthese.R
import ca.qc.cstj.tpsynthese.domain.models.Ticket
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TicketFragment : Fragment(R.layout.fragment_ticket) {

    private val binding: FragmentTicketBinding by viewBinding()
    private val viewModel: TicketViewModel by viewModels()

    private lateinit var ticketRecyclerViewAdapter: TicketRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ticketRecyclerViewAdapter = TicketRecyclerViewAdapter(listOf(), ::onRecyclerViewTicketClick)

        binding.rxcTickets.apply {
            layoutManager = GridLayoutManager(requireContext(), 1)
            adapter = ticketRecyclerViewAdapter
        }

        viewModel.ticketUiState.onEach {
            when(it) {
                is TicketUiState.Error -> {
                    Log.e("Test",it.exception.toString())
                    Toast.makeText(requireContext(), it.exception?.localizedMessage ?: getString(R.string.apiErrorMessage), Toast.LENGTH_SHORT).show()
                }
                TicketUiState.Loading -> {
                    binding.rxcTickets.visibility = View.GONE
                }
                is TicketUiState.Success -> {
                    binding.rxcTickets.visibility = View.VISIBLE
                    ticketRecyclerViewAdapter.tickets = it.ticket
                    ticketRecyclerViewAdapter.notifyDataSetChanged()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun onRecyclerViewTicketClick(ticket: Ticket) {
        val action = TicketFragmentDirections.actionNavigationTicketToDetailTicketFragment(ticket.href)
        findNavController().navigate(action)
    }
}