package ca.qc.cstj.tenretni.core

object Constants {

    object BaseURL {
        //private const val BASE_API = "http://10.0.2.2:5000"
        const val BASE_API = "https://api.andromia.science"
        const val TICKETS = "/tickets"
        const val CUSTOMERS = "/customers"
        const val GATEWAYS = "/gateways"
        const val NETWORK = "/network"
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
        const val LOADING_DELAY: Long = 1000L
    }

    object RefreshDelay {
        const val LIST_NETWORK = 120000L
        const val LIST_TICKET = 30000L
        const val LIST_GATEWAYS = 60000L
        const val DETAIL_TICKET = 30000L
    }

}