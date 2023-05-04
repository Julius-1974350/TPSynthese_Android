package ca.qc.cstj.tpsynthese.ui.gateway.detail

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ca.qc.cstj.tpsynthese.R
import ca.qc.cstj.tpsynthese.databinding.FragmentDetailGatewayBinding
import ca.qc.cstj.tpsynthese.ui.ticket.detail.DetailTicketViewModel

class DetailGatewayFragment:Fragment(R.layout.fragment_detail_gateway) {
    private val binding: FragmentDetailGatewayBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnReboot.setOnClickListener {
            //viewModel.rebootGateway(args.href)
        }
        binding.btnReboot.setOnClickListener {
            //viewModel.updateGateway(args.href)
        }
    }
}