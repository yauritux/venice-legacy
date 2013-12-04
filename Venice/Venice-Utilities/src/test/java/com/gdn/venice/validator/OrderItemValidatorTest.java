package com.gdn.venice.validator;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.gdn.integration.jaxb.LogisticsInfo;
import com.gdn.integration.jaxb.LogisticsProvider;
import com.gdn.integration.jaxb.OrderItem;
import com.gdn.integration.jaxb.Party;
import com.gdn.venice.exception.InvalidOrderItemException;

@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:OrderItemValidatorTest-context.xml"})
public class OrderItemValidatorTest {
	
	@Autowired
	@InjectMocks
	private OrderItemValidator orderItemValidator;
	
	@Mock
	private OrderItem orderItem;
	
	@Mock
	private LogisticsInfo logisticsInfo;
	
	@Mock
	private LogisticsProvider logisticsProvider;
	
	@Mock
	private Party party;
	
	@Before
	public void setup() {		
		MockitoAnnotations.initMocks(this);
		when(orderItem.getLogisticsInfo()).thenAnswer(new Answer<LogisticsInfo> () {
			public LogisticsInfo answer(InvocationOnMock invocation) {
				return logisticsInfo;
			}
		});
		when(logisticsInfo.getLogisticsProvider()).thenAnswer(new Answer<LogisticsProvider> () {
			public LogisticsProvider answer(InvocationOnMock invocation) {
				return logisticsProvider;
			}
		});
		when(logisticsProvider.getParty()).thenAnswer(new Answer<Party> () {
			public Party answer(InvocationOnMock invocation) {
				return party;
			}
		});
		when(party.getFullOrLegalName())
		   .thenAnswer(new Answer<String> () {		
			public String answer(InvocationOnMock invocation) {				
				//Object[] args = invocation.getArguments();
				//Object mock = invocation.getMock();				
				return "Select Shipping";
			}
		});				
	}

	@Test(expected = InvalidOrderItemException.class, timeout = 1000)
	public void isShippingPartyProvided_NoPartySelected_VENEX000011Caught() throws Exception {
		orderItemValidator.isOrderItemValid(orderItem);
	}

}
