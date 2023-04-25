package ca.qc.cstj.tpsynthese.ui.gateway

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ca.qc.cstj.tpsynthese.databinding.FragmentGatewayBinding

class GatewayFragment : Fragment() {

    private var _binding: FragmentGatewayBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val gatewayViewModel =
            ViewModelProvider(this).get(GatewayViewModel::class.java)

        _binding = FragmentGatewayBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGateway
        gatewayViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}