/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.dao.impl;

import it.eng.areas.ems.sdoemsrepo.dao.MessageListRepository;
import it.eng.areas.ems.sdoemsrepo.delegate.model.Message;

/**
 * @author Bifulco Luigi
 *
 */
//@Repository
public class MessageListRepositoryImpl implements MessageListRepository {

	/* (non-Javadoc)
	 * @see it.eng.areas.ems.sdoemsrepo.dao.MessageListRepository#popMessage(java.lang.String)
	 */
	@Override
	public Message popMessage(String queueName) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see it.eng.areas.ems.sdoemsrepo.dao.MessageListRepository#pushMessage(java.lang.String, it.eng.areas.ems.sdoemsrepo.delegate.model.Message)
	 */
	@Override
	public void pushMessage(String queueName, Message m) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see it.eng.areas.ems.sdoemsrepo.dao.MessageListRepository#countMessages(java.lang.String)
	 */
	@Override
	public long countMessages(String queueName) {
		// TODO Auto-generated method stub
		return 0;
	}

//	@Autowired
//	private RedisTemplate<String, Message> messageRedisTemplate;
//
//	private ListOperations<String, Message> listOps;
//
//	@PostConstruct
//	public void init() {
//		listOps = messageRedisTemplate.opsForList();
//	}
//
//	@Override
//	public Message popMessage(String queueName) {
//		return listOps.rightPop(queueName);
//	}
//
//	@Override
//	public void pushMessage(String queueName, Message m) {
//		listOps.leftPush(queueName, m);
//	}
//
//	/* (non-Javadoc)
//	 * @see it.eng.areas.ems.sdoemsrepo.dao.MessageListRepository#countMessages(java.lang.String)
//	 */
//	@Override
//	public long countMessages(String queueName) {
//		return listOps.size(queueName);
//	}
}
