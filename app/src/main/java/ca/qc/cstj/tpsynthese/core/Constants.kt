package ca.qc.cstj.tenretni.core

object Constants {

    object BaseURL {
        //private const val BASE_API = "http://10.0.2.2:5000"
        private const val BASE_API = "https://api.andromia.science"
        const val TICKETS = "$BASE_API/tickets"
        const val CUSTOMERS = "$BASE_API/customers"
        const val GATEWAYS = "$BASE_API/gateways"
        const val NETWORK = "$BASE_API/network"
        const val TICKETFORTEST = "$BASE_API/tickets/1"
    }

    const val FLAG_API_URL = "https://flagcdn.com/h40/%s.png"

    enum class TicketPriority {
        Low, Normal, High, Critical
    }

    enum class TicketStatus {
        Open, Solved
    }

    enum class ConnectionStatus {
        Online, Offline
    }
    object Delay {
        const val LOADING_DELAY: Long = 10000L
    }

    object RefreshDelay {
        const val LIST_TICKET = 30000L
    }

}