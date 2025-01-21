package sa.sauditourism.employee.managers.network.exceptions

import java.io.IOException

class TokenExpiredException constructor(msg: String = "") : IOException(msg)