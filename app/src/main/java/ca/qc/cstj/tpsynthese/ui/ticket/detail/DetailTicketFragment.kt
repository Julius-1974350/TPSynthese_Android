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
import ca.qc.cstj.tpsynthese.R
import ca.qc.cstj.tpsynthese.databinding.FragmentDetailTicketBinding
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import ca.qc.cstj.tenretni.core.ColorHelper
import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tpsynthese.domain.models.Gateway
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import kotlinx.coroutines.flow.launchIn
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DetailTicketFragment: Fragment(R.layout.fragment_detail_ticket) {
    private val binding:FragmentDetailTicketBinding by viewBinding()
    private val args: DetailTicketFragmentArgs by navArgs()
    private lateinit var DetailTicketRecyclerViewAdapter: DetailTicketRecyclerViewAdapter
    private val viewModel: DetailTicketViewModel by viewModels {
        DetailTicketViewModel.Factory(args.href)
    }
    private var hrefCustomer: String = ""
    private val scanQRCode = registerForActivityResult(ScanQRCode(), ::handleQRResult)

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DetailTicketRecyclerViewAdapter = DetailTicketRecyclerViewAdapter(listOf(), ::onRecyclerViewGatewayClick)
        binding.rcvGateways.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = DetailTicketRecyclerViewAdapter
        }
        binding.btnInstall.setOnClickListener {
            scanQRCode.launch(null)
        }
        viewModel.detailTicketUiState.onEach {
            when(it){
                is DetailTicketUIState.Error -> {
                    Toast.makeText(requireContext(), it.exception?.localizedMessage ?: getString(R.string.apiErrorMessage), Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
                DetailTicketUIState.Loading -> Unit
                is DetailTicketUIState.SuccessTicket -> {
                     //TODO : Change the title // getString(R.string.txvTicketCode,it.ticket.ticketNumber)
                    // Add data into the ticket item
                    hrefCustomer = it.ticket.customer.href
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

                    // get the gateways of the customer
                    viewModel.getGateways(it.ticket.customer);

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
    private fun onRecyclerViewGatewayClick(gateway: Gateway) {
        // TODO : Make navigation
        // val action = TicketFragmentDirections.actionNavigationTicketToDetailTicketFragment(gateway.href)
        // findNavController().navigate(action)
    }

}