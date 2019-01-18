<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>${classExplainDto.explain}</title>
<link href="style.css" rel="stylesheet" />
</head>
<body>
	<div id="wmd-preview" class="wmd-preview">
		<h1>
			<a href="index.html">《首页</a><span>${classExplainDto.explain}</span>
		</h1>
		<#list methodDescriptions as methodDescription>
		<ul class="toc">
			<li><a href="#${methodDescription}">${methodDescription}</a></li>
		</ul>
		</#list>
		<hr>
		<#list methodInfoDtos as methodInfoDto>
		<div>
			<h2 id="${methodInfoDto.methodDescription}">${methodInfoDto.methodDescription}</h2>
			<div class="prettyprint lang-json">
				<div>
					<label>method：</label><span>${methodInfoDto.type}</span>
				</div>
				<div>
					<label>url:</label><span>${methodInfoDto.url}</span>
				</div>
			</div>
			<p>
				<strong>参数列表</strong>
			</p>
			<table>
				<tr>
					<th>参数名</th>
					<th>类型</th>
					<th>描述</th>
					<th>是否必传</th>
				</tr>
				<#list methodInfoDto.requestParamDtos as requestParamDto>
				<tr>
					<td>${requestParamDto.name}</td>
					<td>${requestParamDto.type}</td>
					<td>${requestParamDto.description}</td>
					<td>${requestParamDto.required?string('YES' , 'NO')}</td>
				</tr>
				</#list>
			</table>
			<p>返回结果:</p>
			<div class="prettyprint lang-json">
				<div>
					{<br />
					<#if methodInfoDto.baseResponseDataDtos?? && methodInfoDto.baseResponseDataDtos?size gt 0>
						<#list methodInfoDto.baseResponseDataDtos as baseResponseDataDto>
							<div style="margin-left: 20px;">
								"<span>${baseResponseDataDto.name}</span>": "<span>${baseResponseDataDto.type}</span>
								//<span>${baseResponseDataDto.description}</span>"
							</div> 
							<#if baseResponseDataDto.responseDataDtos?? && baseResponseDataDto.responseDataDtos?size gt 0>
								<#list baseResponseDataDto.responseDataDtos as responseClassDto>
									<#if responseClassDto_index=0>
									<div style="margin-left: 20px;">
										<#--[<br>-->
									</#if>
									<div style="margin-left: 20px;">
										<#if responseClassDto_index gt 0 && (baseResponseDataDto.responseDataDtos[responseClassDto_index - 1].className !=responseClassDto.className)>
										<div>}</div>
										</#if>
										<#if responseClassDto_index=0 || (baseResponseDataDto.responseDataDtos[responseClassDto_index - 1].className !=responseClassDto.className)>
										<div>
											<span>{</span>
										</div>
										</#if>
										<div style="margin-left: 20px;">
											"<span>${responseClassDto.name}</span>": "
											<#if responseClassDto.type='class'
												|| responseClassDto.type='list'>{</#if>
											<span>${responseClassDto.type}</span> // <span>${responseClassDto.description}</span>"
											<#if responseClassDto.type = 'list' && !responseClassDto.responseDataDtos??>
											<div>}</div>
											</#if>
										</div>
										<#if responseClassDto.responseDataDtos?? && responseClassDto.responseDataDtos?size gt 0>
											<#list responseClassDto.responseDataDtos as responseDataDto>
												<div style="margin-left: 20px; padding-left: 20px;">
													"<span>${responseDataDto.name}</span>":
													<#if responseDataDto.type='list' || responseDataDto.type='class'>{</#if>
													"<span>${responseDataDto.type}</span> // <span>${responseDataDto.description}</span>"
													<#if responseDataDto.responseDataDtos?? && responseDataDto.responseDataDtos?size gt 0>
														<#list responseDataDto.responseDataDtos as respDataDto>
															<div style="margin-left: 20px;">
																<div style="padding-left: 20px;">
																	"<span>${respDataDto.name}</span>": "
																	<#if respDataDto_index=0> <span>{</span> </#if>
																	<span>${respDataDto.type}</span> // <span>${respDataDto.description}</span>"
																</div>
																<#if !respDataDto_has_next> 
																	<span>}</span> 
																</#if>
															</div>
														</#list> 
													</#if>
												</div>
												<#if !responseDataDto_has_next>
												<div style="margin-left: 20px;"></div>
												</#if> 
											</#list> 
										</#if>
										<#if !responseClassDto_has_next>
										<div>}</div>
										</#if>
									</div>
									<#--<#if !responseClassDto_has_next> ] </#if> -->
								</#list> 
							</#if>
						</#list>
					<#elseif methodInfoDto.responseClassDtos?? && methodInfoDto.responseClassDtos?size gt 0>
						<#list methodInfoDto.responseClassDtos as responseClassDto> 
							<div style="margin-left: 20px;">
								<div>
									"<span>${responseClassDto.className}</span>": {
								</div>
								<#list responseClassDto.responseDataDtos as responseData1Dto>
									<div style="margin-left: 20px;">
										"<span>${responseData1Dto.name}</span>": "
										<#if responseData1Dto.type== 'list' || responseData1Dto.type=='class'>
											<span>{</<span>
										</#if>
										<span>${responseData1Dto.type}</span> // <span>${responseData1Dto.description}</span>"
										<#if responseData1Dto.type== 'list' || responseData1Dto.type=='class'>
											<#if !responseData1Dto.responseDataDtos??>
												<div>}</div>
											</#if>
										</#if>
										<#if responseData1Dto.responseDataDtos?? && responseData1Dto.responseDataDtos?size gt 0>
											<#list responseData1Dto.responseDataDtos as responseData2Dto>
												<div style="margin-left: 20px;">
													<div style="padding-left: 20px;">
														"<span>${responseData2Dto.name}</span>": " <span>${responseData2Dto.type}</span>
														// <span>${responseData2Dto.description}</span>"
														<#if responseData2Dto.responseDataDtos?? && responseData2Dto.responseDataDtos?size gt 0>
															<#list responseData2Dto.responseDataDtos as responseData3Dto>
															<div style="margin-left: 20px;">
																<div style="padding-left: 20px;">
																	"<span>${responseData3Dto.name}</span>": "
																	<#if responseData3Dto.type== 'list' || responseData3Dto.type=='class'>
																	<span>{</<span>
																	</#if>
																	<span>${responseData3Dto.type}</span> // <span>${responseData3Dto.description}</span>"
																</div>
																<#if !responseData3Dto_has_next>
																<div style="margin-left: -20px;">
																	}</
																	<div>
																</#if>
															</div>
															</#list> 
														</#if>
													</div>
													<#if !responseData2Dto_has_next>
														<div style="margin-left: -20px;">}</<div>
													</#if>
												</div>
											</#list> 
										</#if>
									</div>
								</#list>
								<div style="margin-left: -20px;">}</div>
							</div>
						</#list>
					<#else> 
					</#if>
					</div>
		</#list>
		<div>}</div>
		<hr>
	</div>
	
</body>

</html>