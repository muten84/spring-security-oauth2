package it.luigibifulco.microservice.cloud.counterservice;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class TransactionalCounterService {

	@Autowired
	private CounterRepository repository;

	public CounterBean lastCount() {
		List<CounterBean> list = this.repository.findAll(Sort.by(Order.desc("countTime")));
		if (list == null || list.isEmpty())
			list = null;

		CounterBean bean = list != null ? list.get(0) : new CounterBean(0);
		return bean;
	}

	@Modifying
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public CounterBean increment() {
		List<CounterBean> list = this.repository.findAll(Sort.by(Order.desc("countTime")));
		if (list == null || list.isEmpty())
			list = null;

		CounterBean bean = list != null ? list.get(0) : new CounterBean(0);
		CounterBean newBean = new CounterBean();
		newBean.setCountTime(Calendar.getInstance().getTime());

		newBean.setCurrentCount(bean.getCurrentCount() + 1);
		return this.repository.save(newBean);
	}

}
