package com.ld33.game.pawn;

import com.ld33.Config;
import com.ld33.utils.SpriteActor;
import com.ld33.utils.steps.FloatStep;
import com.ld33.utils.steps.Steps;

public abstract class Pawn extends SpriteActor {
	
	private float modY;
	private final FloatStep.Listener modYListener = new FloatStep.Listener() {
		@Override
		public void onChange(final FloatStep floatStep,
							 final float value) {
			
			modY = value;
		}
	};
	
	public static enum CombatType {
		MELEE, RANGED, MAGICAL
	};
	
	/* Stats */
	private final float maxHealth;
	private float health;
	private CombatType combatType;
	private float visionRange;
	private float attackRange;
	private float attackInterval;
	private float timeUntilAttack = 0;
	private float movementSpeed;
	private float projectileSpeed;
	private float damage;
	
	public Pawn(final float maxHealth) {
		/* Set stats TODO remove hardcoding*/
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		this.combatType = CombatType.RANGED;
		this.visionRange = 250f;
		if(combatType == CombatType.MELEE) {
			this.attackRange = Config.MeleeAttackRange;
			this.attackInterval = Config.MeleeAttackInterval;
			this.damage = 3f;
			this.projectileSpeed = 120f;
		}
		else if(combatType == CombatType.RANGED) {
			this.attackRange = Config.RangedAttackRange;
			this.attackInterval = Config.RangedAttackInterval;
			this.damage = 3f;
			this.projectileSpeed = 120f;
		}
		else if(combatType == CombatType.MAGICAL) {
			this.attackRange = Config.MagicalAttackRange;
			this.attackInterval = Config.MagicalAttackInterval;
			this.damage = 3f;
			this.projectileSpeed = 120f;
		}
		this.movementSpeed = 2f;
		
	}
	
	public boolean canAttack() {
		return timeUntilAttack <= 0;
	}
	
	public void resetAttackCooldown() {
		timeUntilAttack = attackInterval;
	}
	
	public float getMaxHealth() {
		return maxHealth;
	}
	
	public float getHealth() {
		return health;
	}
	
	public float getAttackRange() {
		return attackRange;
	}

	public float getDamage() {
		return damage;
	}

	public float getProjectileSpeed() {
		return projectileSpeed;
	}

	public void damagePawn(final float damage) {
		this.health -= damage;
		
		if(health < 0f) {
			health = 0f;
		}
	}

	public float getMovementSpeed() {
		return movementSpeed;
	}

	public float getPlaneY() {
		return getY() - (getHeight() * Config.PawnAnimationJumpHeight) * modY;
	}
	
	public float getCollisionHeight() {
		return getWidth();
	}
	
	public float getJumpDisplacement() {
		return getHeight() * Config.PawnAnimationJumpHeight * modY;
	}
	
	@Override
	public void act(final float delta) {
		final float jumpHeight = getHeight() * Config.PawnAnimationJumpHeight;
		
		// Important! process before calling act
		final float realY = getY() - jumpHeight * modY;
		
		super.act(delta);
		
		setY(realY + jumpHeight * modY);
		
		//Combat stuff
		timeUntilAttack -= delta;
		if(timeUntilAttack < 0) timeUntilAttack = 0;
	}
	
	protected void startMovementAnimation() {
		addAction(Steps.action(Steps.repeat(Steps.sequence(
				  Steps._float(0f, 1f, (Config.PawnAnimationJumpDuration / 2f), modYListener),
				  Steps._float(1f, 0f, (Config.PawnAnimationJumpDuration / 2f), modYListener)))));
	}
	
	protected void endMovementAnimation() {
		clearActions();
		addAction(Steps.action(Steps._float(modY, 0, modY * (Config.PawnAnimationJumpDuration / 2f), modYListener)));
	}
}
