/**
 * 
 */
package it.eng.areas.ems.mobileagent.message;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.pmw.tinylog.Logger;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import it.eng.areas.ems.sdoemsrepo.delegate.model.Message;

/**
 * @author Bifulco Luigi
 *
 */
public class MessageProcessor {
	ThreadFactory s1 = new ThreadFactoryBuilder().setNameFormat("MessageProcessorSyncWorker-thread-%d").build();
	ThreadFactory s2 = new ThreadFactoryBuilder().setNameFormat("MessageProcessorAsyncWorker-thread-%d").build();

	private ExecutorService synchMessageWorker = Executors.newSingleThreadExecutor(s1);
	private ExecutorService asynchMessageWorker = Executors.newSingleThreadExecutor(s2);

	long timeout;

	public MessageProcessor(long timeoutInMillis) {
		this.timeout = timeoutInMillis;
	}
	
	public <Req,Res> Res invoke(Req req, long timeout, RequestHandler<Req,Res> handler) throws MessageProcessingException{
		try {
			return synchMessageWorker.submit(new Callable<Res>() {

				@Override
				public Res call() throws Exception {
					return handler.handle(req);
				}
				
			}).get(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			Logger.error(e,"Exception during request synch:");
			throw new MessageProcessingException(null, "Exception during request synch: "+timeout, e);
		}
	}
	
	public Message sendSynch(Message m, long timeout, MessageHandler handler) throws MessageProcessingException {
		try {
			return synchMessageWorker.submit(new Callable<Message>() {

				@Override
				public Message call() throws Exception {
					return handler.handle(m);
				}
			}).get(timeout, TimeUnit.MILLISECONDS);

		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			Logger.error(e,"Exception during synch message processing");
			throw new MessageProcessingException(m, "Exception during synch message processing", e);
		}

	}

	public boolean sendAsynch(Message m, MessageHandler handler) {
		Future<Message> f = asynchMessageWorker.submit(new Callable<Message>() {

			@Override
			public Message call() throws Exception {
				return handler.handle(m);
			}
		});
		return f != null;
	}
	
	public boolean shutdownMessageProcessor() {
		//TODO memorizzare messaggi pendendi nel documento store.
		this.synchMessageWorker.shutdownNow();
		this.asynchMessageWorker.shutdownNow();
		return true;
	}

}
