package ca.qc.cstj.tpsynthese.ui.ticket.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import ca.qc.cstj.tpsynthese.R
import ca.qc.cstj.tpsynthese.databinding.FragmentDetailTicketBinding
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import ca.qc.cstj.tenretni.core.ColorHelper
import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tpsynthese.domain.models.Customer
import ca.qc.cstj.tpsynthese.domain.models.Gateway
import ca.qc.cstj.tpsynthese.domain.models.Ticket
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import kotlinx.coroutines.flow.launchIn
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.onEach

class DetailTicketFragment: Fragment(R.layout.fragment_detail_ticket) {
    private val binding:FragmentDetailTicketBinding by viewBinding()
    private val args: DetailTicketFragmentArgs by navArgs()
    private lateinit var DetailTicketRecyclerViewAdapter: DetailTicketRecyclerViewAdapter
    private val viewModel: DetailTicketViewModel by viewModels {
        DetailTicketViewModel.Factory(args.href)
    }
    private var position : LatLng? = null
    private var username = ""
    private var hrefCustomer: String = ""
    private val scanQRCode = registerForActivityResult(ScanQRCode(), ::handleQRResult)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onResume()
    }
    override fun onResume() {
        super.onResume()
        showTicket()
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun showTicket() {
        DetailTicketRecyclerViewAdapter = DetailTicketRecyclerViewAdapter(listOf(), ::onRecyclerViewGatewayClick)
        binding.rcvGateways.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = DetailTicketRecyclerViewAdapter
        }
        viewModel.detailTicketUiState.onEach {
            when(it){
                is DetailTicketUIState.Error -> {
                    Toast.makeText(requireContext(), it.exception?.localizedMessage ?: getString(R.string.apiErrorMessage), Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
                DetailTicketUIState.Loading -> Unit
                is DetailTicketUIState.SuccessTicket -> {
                    DisplayTicket(it.ticket)
                }
                is DetailTicketUIState.SuccessGateways -> {
                    DetailTicketRecyclerViewAdapter.gateways = it.gateways
                    DetailTicketRecyclerViewAdapter.notifyDataSetChanged()
                }
                is DetailTicketUIState.GatewayInstallError -> Toast.makeText(requireContext(), it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                is DetailTicketUIState.GatewayInstallSuccess -> {
                    DetailTicketRecyclerViewAdapter.gateways = it.gateways
                    DetailTicketRecyclerViewAdapter.notifyDataSetChanged()
                    Toast.makeText(requireContext(), R.string.qr_success, Toast.LENGTH_SHORT).show()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.fabLocation.setOnClickListener {
            if (position != null) {
                val action = DetailTicketFragmentDirections.actionDetailTicketFragmentToMapsActivity(position!!, username)
                findNavController().navigate(action)
            }
        }
        binding.btnSolve.setOnClickListener {
            viewModel.solveTicket()
        }
        binding.btnOpen.setOnClickListener {
            viewModel.openTicket()
        }
        binding.btnInstall.setOnClickListener {
            scanQRCode.launch(null)
        }
    }
    private fun onRecyclerViewGatewayClick(gateway: Gateway) {
        // TODO : Make navigation
        // val action = TicketFragmentDirections.actionNavigationTicketToDetailTicketFragment(gateway.href)
        // findNavController().navigate(action)
    }
    private fun handleQRResult(qrResult: QRResult) {
        when(qrResult) {
            is QRResult.QRSuccess -> {
                viewModel.addGateway(qrResult.content.rawValue, hrefCustomer)
            }
            QRResult.QRUserCanceled -> Toast.makeText(requireContext(),getString(R.string.user_canceled), Toast.LENGTH_SHORT).show()
            QRResult.QRMissingPermission -> Toast.makeText(requireContext(),getString(R.string.missing_permission), Toast.LENGTH_SHORT).show()
            is QRResult.QRError -> Toast.makeText(requireContext(),qrResult.exception.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
    private fun getUserInfoMap(customer: Customer)
    {
        if (customer.coord != null)
        {
            position = LatLng(customer.coord.latitude.toDouble(), customer.coord.longitude.toDouble())
            username = customer.firstName + " " + customer.lastName
        }
    }
    private fun DisplayTicket(ticket : Ticket){
        // Add data into the ticket item
        hrefCustomer = ticket.customer.href
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Ticket " + ticket.ticketNumber
        binding.ItemDetailTicket.txvTicketCode.text = getString(R.string.txvTicketCode, ticket.ticketNumber)
        binding.ItemDetailTicket.txvTicketDate.text = ticket.createdDate
        binding.ItemDetailTicket.chipPriority.text = ticket.priority
        binding.ItemDetailTicket.chipStatus.text = ticket.status
        binding.ItemDetailTicket.chipStatus.chipBackgroundColor = ColorHelper.ticketStatusColor(requireContext(), ticket.status)
        binding.ItemDetailTicket.chipPriority.chipBackgroundColor = ColorHelper.ticketPriorityColor(requireContext(),ticket.priority)
        // Customer data
        binding.txvName.text = "${ticket.customer.firstName}  ${ticket.customer.lastName}"
        binding.txvAdress.text = ticket.customer.address
        binding.txvCity.text = ticket.customer.city
        Glide.with(requireContext())
            .load(Constants.FLAG_API_URL.format(ticket.customer.country?.lowercase()))
            .into(binding.imvCountry)
        // get args coodinate and username for the map
        getUserInfoMap(ticket.customer)
        // get the gateways of the customer
        viewModel.getGateways(ticket.customer);
        // Hide and Show the button depent by the initial status
        // TODO : ask yannick how to check status with constant without a toString()
        if (ticket.status == Constants.TicketStatus.Solved.toString())
        {
            binding.btnOpen.visibility = View.VISIBLE
            binding.btnSolve.visibility = View.GONE
            binding.btnInstall.visibility = View.GONE
        } else {
            binding.btnOpen.visibility = View.GONE
            binding.btnSolve.visibility = View.VISIBLE
            binding.btnInstall.visibility = View.VISIBLE
        }
    }

}