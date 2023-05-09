package ca.qc.cstj.tpsynthese.ui.gateway.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import ca.qc.cstj.tenretni.core.ColorHelper
import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tenretni.core.loadFromResource
import ca.qc.cstj.tpsynthese.R
import ca.qc.cstj.tpsynthese.databinding.FragmentDetailGatewayBinding
import ca.qc.cstj.tpsynthese.domain.models.Gateway
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DetailGatewayFragment : Fragment(R.layout.fragment_detail_gateway){

    private val args: DetailGatewayFragmentArgs by navArgs()

    private val binding: FragmentDetailGatewayBinding by viewBinding()
    private val viewModel: DetailGatewayViewModel by viewModels {
        DetailGatewayViewModel.Factory(args.href)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.detailGatewayUiState.onEach {
            when(it) {
                DetailGatewayUIState.Empty -> Unit
                is DetailGatewayUIState.Error -> {
                    Toast.makeText(requireContext(), it.exception?.localizedMessage, Toast.LENGTH_LONG).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
                DetailGatewayUIState.Loading -> Unit
                is DetailGatewayUIState.Success -> {
                    displayGateway(it.gateway)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun displayGateway(gateway: Gateway) {
        with(binding) {
            StatusChip.text = gateway.connection.status
            StatusChip.chipBackgroundColor = ColorHelper.connectionStatusColor(root.context, gateway.connection.status)
            txvSerialNumber.text = gateway.serialNumber
            txvMAC.text = gateway.config.mac
            txvSSID.text = root.context.getString(R.string.txvSSID, gateway.config.SSID)
            txvPin.text = root.context.getString(R.string.txvPin, gateway.pin)

            if (gateway.config.kernel != null)
            {
                imvElement1.loadFromResource(requireContext(), "element_${gateway.config.kernel[0].lowercase()}")
                imvElement2.loadFromResource(requireContext(), "element_${gateway.config.kernel[1].lowercase()}")
                imvElement3.loadFromResource(requireContext(), "element_${gateway.config.kernel[2].lowercase()}")
                imvElement4.loadFromResource(requireContext(), "element_${gateway.config.kernel[3].lowercase()}")
                imvElement5.loadFromResource(requireContext(), "element_${gateway.config.kernel[4].lowercase()}")
            }
            txvKernelRevision.text = root.context.getString(R.string.txvKernelRevision, gateway.config.kernelRevision.toString())
            txvVersion.text = root.context.getString(R.string.txvVersion, gateway.config.version)
        }
        if (gateway.connection.status == "Online") {
            with(binding) {
                txvIp.text = gateway.connection.ip
                txvLatence.text = root.context.getString(R.string.txvPing, gateway.connection.ping.toString())
                txvDownload.text = root.context.getString(R.string.txvUploadDownload, gateway.connection.download.toString())
                txvUpload.text = root.context.getString(R.string.txvUploadDownload, gateway.connection.upload.toString())
                txvSignalQuality.text = root.context.getString(R.string.txvSignalQuality, gateway.connection.signal.toString())
                if(gateway.connection.signal != null) txvSignalQuality.setTextColor(ColorHelper.connectionSignalColor(root.context, gateway.connection.signal))
            }
        } else {
            with(binding) {
                txvIp.text = Constants.NO_FIND
                txvLatence.text = Constants.NO_FIND
                txvDownload.text = Constants.NO_FIND
                txvUpload.text = Constants.NO_FIND
                txvSignalQuality.text = Constants.NO_FIND
            }
        }
        binding.btnReboot.setOnClickListener {
            viewModel.rebootGateway(args.href)
        }
        binding.btnUpdate.setOnClickListener {
            viewModel.updateGateway(args.href)
        }
    }



}