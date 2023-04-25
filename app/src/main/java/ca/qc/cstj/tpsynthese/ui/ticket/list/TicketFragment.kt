package ca.qc.cstj.tpsynthese.ui.ticket.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
<<<<<<< HEAD:app/src/main/java/ca/qc/cstj/tpsynthese/ui/ticket/TicketFragment.kt
import ca.qc.cstj.tpsynthese.databinding.FragmentTicketBinding
=======
import ca.qc.cstj.tpsynthese.R
>>>>>>> William:app/src/main/java/ca/qc/cstj/tpsynthese/ui/ticket/list/TicketFragment.kt

class TicketFragment : Fragment(R.layout.fragment_list_tickets) {

    private var _binding: FragmentTicketBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val ticketViewModel =
            ViewModelProvider(this).get(TicketViewModel::class.java)

        _binding = FragmentTicketBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textTicket
        ticketViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}