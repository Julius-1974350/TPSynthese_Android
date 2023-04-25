package ca.qc.cstj.tpsynthese.ui.ticket.detail

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ca.qc.cstj.tpsynthese.R
import ca.qc.cstj.tpsynthese.databinding.FragmentDetailTicketBinding
import androidx.navigation.fragment.navArgs
import ca.qc.cstj.tenretni.core.Constants
import kotlinx.coroutines.flow.onEach

class DetailTicketFragment: Fragment(R.layout.fragment_detail_ticket) {
    private val binding:FragmentDetailTicketBinding by viewBinding()
    private val viewModel: DetailTicketViewModel by viewModels {
        DetailTicketViewModel.Factory(Constants.BaseURL.TICKETFORTEST)
    }
    // private val args: DetailTicketFragment by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.detailTicketUiState.onEach {
            when(it){
                is DetailTicketUIState.Error -> {
                    Toast.makeText(requireContext(), it.exception?.localizedMessage ?: getString(R.string.apiErrorMessage), Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
                DetailTicketUIState.Loading -> Unit
                is DetailTicketUIState.Success -> {
                    // TODO: update the ui
                }
            }
        }
    }

}