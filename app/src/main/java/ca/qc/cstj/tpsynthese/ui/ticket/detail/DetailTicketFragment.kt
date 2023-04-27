package ca.qc.cstj.tpsynthese.ui.ticket.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tpsynthese.R
import ca.qc.cstj.tpsynthese.databinding.FragmentDetailTicketBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DetailTicketFragment: Fragment(R.layout.fragment_detail_ticket) {
    private val binding:FragmentDetailTicketBinding by viewBinding()
    private val args: DetailTicketFragmentArgs by navArgs()
    private val viewModel: DetailTicketViewModel by viewModels {
        DetailTicketViewModel.Factory(args.href)
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.detailTicketUiState.onEach {
            when(it){
                is DetailTicketUIState.Error -> {
                    Toast.makeText(requireContext(), it.exception?.localizedMessage ?: getString(R.string.apiErrorMessage), Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
                DetailTicketUIState.Loading -> Unit
                is DetailTicketUIState.SuccessTicket -> {
                    // Add data into the ticket item
                    binding.ItemDetailTicket.txvTicketCode.text ="Ticket " +  it.ticket.ticketNumber
                    binding.ItemDetailTicket.txvTicketDate.text = it.ticket.createdDate

                    // Customer data
                    binding.txvName.text = it.ticket.customer.firstName + it.ticket.customer.lastName
                    binding.txvAdress.text = it.ticket.customer.address
                    binding.txvCity.text = it.ticket.customer.city
                    Glide.with(requireContext())
                        .load(Constants.FLAG_API_URL.format(it.ticket.customer.country?.lowercase()))
                        .into(binding.imvCountry)
                }
                is DetailTicketUIState.SuccessGateways -> TODO()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

}