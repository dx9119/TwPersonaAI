package com.ukhanov.TwPersonaAI.message;

import com.ukhanov.TwPersonaAI.message.model.MessageEntity;
import com.ukhanov.TwPersonaAI.message.model.IMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageEntity toEntity(IMessage message);
}


