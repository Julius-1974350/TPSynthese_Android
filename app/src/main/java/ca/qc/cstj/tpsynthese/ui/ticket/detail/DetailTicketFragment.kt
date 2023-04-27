package ca.qc.cstj.tpsynthese.ui.ticket.detail

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import ca.qc.cstj.tpsynthese.R
import ca.qc.cstj.tpsynthese.databinding.FragmentDetailTicketBinding
import androidx.navigation.fragment.navArgs
import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tpsynthese.domain.models.Gateway
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DetailTicketFragment: Fragment(R.layout.fragment_detail_ticket) {
    private val binding:FragmentDetailTicketBinding by viewBinding()
    private val viewModel: DetailTicketViewModel by viewModels {
        DetailTicketViewModel.Factory(Constants.BaseURL.TICKETFORTEST)
    }
    private val scanQRCode = registerForActivityResult(ScanQRCode(), ::handleQRResult)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                is DetailTicketUIState.Success -> {
                    // TODO: update the ui
                }
                is DetailTicketUIState.GatewayInstallError -> Toast.makeText(requireContext(), it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                is DetailTicketUIState.GatewayInstallSuccess -> Toast.makeText(requireContext(), "Gateway was install successfully", Toast.LENGTH_SHORT).show()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
    private fun handleQRResult(qrResult: QRResult) {
        when(qrResult) {
            is QRResult.QRSuccess -> {
                viewModel.addGateway(qrResult.content.rawValue)
            }
            QRResult.QRUserCanceled -> Toast.makeText(requireContext(),getString(R.string.user_canceled), Toast.LENGTH_SHORT).show()
            QRResult.QRMissingPermission -> Toast.makeText(requireContext(),getString(R.string.missing_permission), Toast.LENGTH_SHORT).show()
            is QRResult.QRError -> Toast.makeText(requireContext(),qrResult.exception.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

}