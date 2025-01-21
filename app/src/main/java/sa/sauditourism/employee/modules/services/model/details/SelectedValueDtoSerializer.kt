//package sa.sauditourism.employee.modules.services.model.details
//
//import kotlinx.serialization.KSerializer
//import kotlinx.serialization.SerializationException
//import kotlinx.serialization.descriptors.SerialDescriptor
//import kotlinx.serialization.descriptors.buildClassSerialDescriptor
//import kotlinx.serialization.descriptors.element
//import kotlinx.serialization.encoding.CompositeDecoder
//import kotlinx.serialization.encoding.Decoder
//import kotlinx.serialization.encoding.Encoder
//import kotlinx.serialization.encoding.decodeStructure
//import kotlinx.serialization.encoding.encodeStructure
//import kotlinx.serialization.json.JsonDecoder
//import kotlinx.serialization.json.JsonObject
//import kotlinx.serialization.json.JsonPrimitive
//import sa.sauditourism.employee.modules.services.model.form.request.RequestFieldValue
//
//class SelectedValueDtoSerializer: KSerializer<Field> {
//
//    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
//        serialName = Field::class.simpleName!!
//    ) {
//        element<String?>("selectedValue")
//    }
//
//    override fun deserialize(decoder: Decoder): Field = decoder.decodeStructure(descriptor) {
//        var selectedValue: String? = null
//
//        while(true) {
//            when(val index = decodeElementIndex(descriptor)) {
//                0 -> {
//                    val jsonDecoder = decoder as? JsonDecoder ?: throw SerializationException(
//                        "This serializer only works with JSON"
//                    )
//                    val element = jsonDecoder.decodeJsonElement()
//
//                    selectedValue = if(element is JsonObject) {
//                        decoder.json.decodeFromJsonElement(
//                            element = element,
//                            deserializer = RequestFieldValue.serializer()
//                        ).label
//                    } else if(element is JsonPrimitive && element.isString) {
//                        element.content
//                    } else null
//                }
//                CompositeDecoder.DECODE_DONE -> break
//                else -> throw SerializationException("Invalid index")
//            }
//        }
//
//        return@decodeStructure Field(selectedValue = selectedValue)
//    }
//
//    override fun serialize(encoder: Encoder, value: Field) = encoder.encodeStructure(
//        descriptor) {
//        value.selectedValue?.let {
//            encodeStringElement(descriptor, 0, it)
//        }
//    }
//}