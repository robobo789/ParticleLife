package task;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.RecursiveAction;

import enviroment.Enviroment;
import main.Config;
import particle.Particle;

public class interactionCalculationTask extends RecursiveAction {
	private Config conf = Config.getInstance();
	private Enviroment env;
	private int from;
	private int to;

	public interactionCalculationTask(Enviroment env, int from, int to) {
		super();
		this.env = env;
		this.from = from;
		this.to = to;
	}

	private void calculateInteractions() {
		for (int i = from; i < to; i++) {
			Particle acter;
			try {
				acter = env.particles.get(i);
				Set<Particle> inInteractionRadius = env.gatherParticlesInInteractionRadius(acter);

				env.acterActs(acter, inInteractionRadius);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	@Override
	protected void compute() {
		if(to - from > conf.taskSize) {
			int mid = (from + to)/2;
			interactionCalculationTask t1 = new interactionCalculationTask(env, from, mid + 1);
			interactionCalculationTask t2 = new interactionCalculationTask(env, mid + 1, to);
			invokeAll(t1, t2);
		}else {
			calculateInteractions();
		}
		
	}
}
