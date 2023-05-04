package ca.qc.cstj.tpsynthese.ui.gateway.detail

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import ca.qc.cstj.tpsynthese.R
import ca.qc.cstj.tpsynthese.databinding.FragmentDetailGatewayBinding
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

                }
            }
        }
    }

}