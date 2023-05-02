package ca.qc.cstj.tpsynthese.ui.ticket.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import ca.qc.cstj.tenretni.core.ColorHelper
import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tpsynthese.R
import ca.qc.cstj.tpsynthese.databinding.FragmentDetailTicketBinding
import ca.qc.cstj.tpsynthese.domain.models.Customer
import ca.qc.cstj.tpsynthese.domain.models.Gateway
import ca.qc.cstj.tpsynthese.domain.models.Ticket
import ca.qc.cstj.tpsynthese.ui.ticket.list.TicketFragmentDirections
import ca.qc.cstj.tpsynthese.ui.ticket.list.TicketRecyclerViewAdapter
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.Console

class DetailTicketFragment: Fragment(R.layout.fragment_detail_ticket) {
    private val binding:FragmentDetailTicketBinding by viewBinding()
    private val args: DetailTicketFragmentArgs by navArgs()
    private lateinit var DetailTicketRecyclerViewAdapter: DetailTicketRecyclerViewAdapter
    private val viewModel: DetailTicketViewModel by viewModels {
        DetailTicketViewModel.Factory(args.href)
    }
    private var position : LatLng? = null
    private var username = ""
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DetailTicketRecyclerViewAdapter = DetailTicketRecyclerViewAdapter(listOf(), ::onRecyclerViewGatewayClick)
        binding.rcvGateways.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = DetailTicketRecyclerViewAdapter
        }

        viewModel.detailTicketUiState.onEach {
            when(it){
                is DetailTicketUIState.Error -> {
                    Log.e("Test",it.exception.toString())
                    Toast.makeText(requireContext(), it.exception?.localizedMessage ?: getString(R.string.apiErrorMessage), Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
                DetailTicketUIState.Loading -> Unit
                is DetailTicketUIState.SuccessTicket -> {
                     //TODO : Change the title // getString(R.string.txvTicketCode,it.ticket.ticketNumber)
                    // Add data into the ticket item
                    (requireActivity() as AppCompatActivity).supportActionBar?.title = "Ticket " + it.ticket.ticketNumber
                    binding.ItemDetailTicket.txvTicketCode.text = getString(R.string.txvTicketCode, it.ticket.ticketNumber)
                    binding.ItemDetailTicket.txvTicketDate.text = it.ticket.createdDate
                    binding.ItemDetailTicket.chipPriority.text = it.ticket.priority
                    binding.ItemDetailTicket.chipStatus.text = it.ticket.status
                    binding.ItemDetailTicket.chipStatus.chipBackgroundColor = ColorHelper.ticketStatusColor(requireContext(), it.ticket.status)
                    binding.ItemDetailTicket.chipPriority.chipBackgroundColor = ColorHelper.ticketPriorityColor(requireContext(),it.ticket.priority)

                    // Customer data
                    binding.txvName.text = it.ticket.customer.firstName + it.ticket.customer.lastName
                    binding.txvAdress.text = it.ticket.customer.address
                    binding.txvCity.text = it.ticket.customer.city
                    Glide.with(requireContext())
                        .load(Constants.FLAG_API_URL.format(it.ticket.customer.country?.lowercase()))
                        .into(binding.imvCountry)

                    // get args coodinate and username for the map
                    getUserInfoMap(it.ticket.customer)
                    // get the gateways of the customer
                    viewModel.getGateways(it.ticket.customer);

                    // Hide and Show the button depent by the initial status
                    if (it.ticket.status == "Solved")
                    {
                        binding.btnOpen.visibility = View.VISIBLE
                        binding.btnSolve.visibility = View.GONE
                        binding.btnInstall.visibility = View.GONE
                    } else if (it.ticket.status == "Open") {
                        binding.btnOpen.visibility = View.GONE
                        binding.btnSolve.visibility = View.VISIBLE
                        binding.btnInstall.visibility = View.VISIBLE
                    }
                }
                is DetailTicketUIState.SuccessGateways -> {
                    DetailTicketRecyclerViewAdapter.gateways = it.gateways
                    DetailTicketRecyclerViewAdapter.notifyDataSetChanged()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        // Button Map
        binding.fabLocation.setOnClickListener {
            if (position != null) {
                val action = DetailTicketFragmentDirections.actionDetailTicketFragmentToMapsActivity(position!!, username)
                findNavController().navigate(action)
            }
        }
        // Button Solve
        binding.btnSolve.setOnClickListener {
            viewModel.solveTicket()
            // TODO: Hide the solve and install button and show the open button
        }
        // Button Open
        binding.btnOpen.setOnClickListener {
            viewModel.openTicket()
            // TODO: Show solve and install button and hide Open
        }

    }
    private fun onRecyclerViewGatewayClick(gateway: Gateway) {
        // TODO : Make navigation
        // val action = TicketFragmentDirections.actionNavigationTicketToDetailTicketFragment(gateway.href)
        // findNavController().navigate(action)
    }

    private fun getUserInfoMap(customer: Customer)
    {
        if (customer.coord != null)
        {
            position = LatLng(customer.coord.latitude.toDouble(), customer.coord.longitude.toDouble())
            username = customer.firstName + " " + customer.lastName
        }
    }

}