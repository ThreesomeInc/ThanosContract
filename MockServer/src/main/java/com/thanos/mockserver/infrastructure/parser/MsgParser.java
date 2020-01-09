package com.thanos.mockserver.infrastructure.parser;

import com.google.common.collect.Maps;
import com.thanos.mockserver.exception.ParseException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Getter
public class MsgParser {


	public Msg parseByTypeAndLength(String inputRequest, List<Schema> requestSchemaList) {
		LinkedHashMap<String, Object> fields = Maps.newLinkedHashMap();

		int startIndex = 0;
		try {
			for (Schema requestSchema : requestSchemaList) {
				fields.put(requestSchema.getName(),
						inputRequest.substring(startIndex, startIndex + requestSchema.getLength()));
				startIndex += requestSchema.getLength();
			}
		} catch (StringIndexOutOfBoundsException stringIndexEx) {
			throw new ParseException("Input Msg is shorter then schema expected!", stringIndexEx.getCause());
		} catch (NumberFormatException numFormatEx) {
			throw new ParseException("Expected to be NUM but turn out to be no!", numFormatEx.getCause());
		}

		return new Msg(fields);
	}

}
