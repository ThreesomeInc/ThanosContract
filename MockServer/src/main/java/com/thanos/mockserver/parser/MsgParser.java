package com.thanos.mockserver.parser;

import com.google.common.collect.Maps;
import com.thanos.mockserver.exception.ParseException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@Getter
public class MsgParser {


	public Msg parseByTypeAndLength(String inputRequest, List<Schema> requestSchemaList) {
		Map<String, Object> fields = Maps.newLinkedHashMap();

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
