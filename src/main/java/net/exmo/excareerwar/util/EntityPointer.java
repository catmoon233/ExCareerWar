package net.exmo.excareerwar.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.PartEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class EntityPointer {

    public static HitResult raycastForEntity(Level level, Entity originEntity, float distance, boolean checkForBlocks) {
        Vec3 start = originEntity.getEyePosition();
        Vec3 end = originEntity.getLookAngle().normalize().scale(distance).add(start);

        return raycastForEntity(level, originEntity, start, end, checkForBlocks);
    }
    public static HitResult checkEntityIntersecting(Entity entity, Vec3 start, Vec3 end, float bbInflation) {
        Vec3 hitPos = null;
        if (entity.isMultipartEntity()) {
            for (PartEntity p : entity.getParts()) {
                var hit = p.getBoundingBox().inflate(bbInflation).clip(start, end).orElse(null);
                if (hit != null) {
                    hitPos = hit;
                    break;
                }
            }
        } else {
            hitPos = entity.getBoundingBox().inflate(bbInflation).clip(start, end).orElse(null);
        }
        if (hitPos != null)
            return new EntityHitResult(entity, hitPos);
        else
            return BlockHitResult.miss(end, Direction.UP, BlockPos.containing(end));

    }

    /**
     * 射线追踪以找到最近的LivingEntity。
     *
     * @param level            世界实例
     * @param originEntity     射线的起源实体
     * @param distance         射线的最大长度
     * @param checkForBlocks   是否检测方块碰撞
     * @return 最近的LivingEntity或null（如果没有找到）
     */
    public static LivingEntity raycastForEntityTo(Level level, Entity originEntity, float distance, boolean checkForBlocks) {
        Vec3 startVec = originEntity.getEyePosition(1.0F); // 获取实体的眼睛位置
        Vec3 lookVec = originEntity.getViewVector(1.0F); // 获取实体的视线向量
        Vec3 endVec = startVec.add(lookVec.x * distance, lookVec.y * distance, lookVec.z * distance); // 计算射线终点

        // 设置射线追踪上下文
        HitResult hitResult = raycast(level, startVec, endVec, checkForBlocks);

        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK && checkForBlocks) {
            return null; // 如果射线击中了方块，则返回null
        }

        // 定义一个边界框用于检测实体
        AABB aabb = new AABB(startVec, endVec).inflate(1, 1, 1);

        // 查找最近的LivingEntity
        LivingEntity closestLivingEntity = null;
        double closestDist = Double.MAX_VALUE;

        for (Entity entity : level.getEntitiesOfClass(LivingEntity.class, aabb, e -> !e.isSpectator() && e != originEntity && e.getHealth() > 0)) {
            Vec3 closestPoint = entity.position().add(0, entity.getBbHeight() / 2, 0); // 实体中心点
            double dist = startVec.distanceToSqr(closestPoint);

            if (dist < closestDist && isOnRay(startVec, endVec, closestPoint)) {
                closestDist = dist;
                closestLivingEntity = (LivingEntity) entity;
            }
        }

        return closestLivingEntity;
    }
    private static HitResult raycast(Level level, Vec3 startVec, Vec3 endVec, boolean checkForBlocks) {
        ClipContext context = new ClipContext(
                startVec,
                endVec,
                checkForBlocks ? ClipContext.Block.COLLIDER : ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                null
        );
        return level.clip(context);
    }

    private static boolean isOnRay(Vec3 startVec, Vec3 endVec, Vec3 point) {
        Vec3 direction = endVec.subtract(startVec);
        Vec3 toPoint = point.subtract(startVec);
        double dotProduct = direction.dot(toPoint);
        double lengthSquared = direction.lengthSqr();

        // Ensure the point is in front of the startVec and within the segment defined by startVec and endVec
        return dotProduct >= 0 && dotProduct <= lengthSquared;
    }


    private static boolean canHitWithRaycast(Entity entity) {
        //IronsSpellbooks.LOGGER.debug("Utils.canHitWithRaycast: {} - {}", entity.getName().getString(), !(entity instanceof Projectile || entity instanceof AreaEffectCloud || entity instanceof ConePart));
        return entity.isPickable() && entity.isAlive();
    }
    public static HitResult raycastForEntity(Level level, Entity originEntity, Vec3 start, Vec3 end, boolean checkForBlocks) {
        return internalRaycastForEntity(level, originEntity, start, end, checkForBlocks, 0, EntityPointer::canHitWithRaycast);
    }
    private static HitResult internalRaycastForEntity(Level level, Entity originEntity, Vec3 start, Vec3 end, boolean checkForBlocks, float bbInflation, Predicate<? super Entity> filter) {
        BlockHitResult blockHitResult = null;
        if (checkForBlocks) {
            blockHitResult = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, originEntity));
            end = blockHitResult.getLocation();
        }
        AABB range = originEntity.getBoundingBox().expandTowards(end.subtract(start));

        List<HitResult> hits = new ArrayList<>();
        List<? extends Entity> entities = level.getEntities(originEntity, range, filter);
        for (Entity target : entities) {
            HitResult hit = checkEntityIntersecting(target, start, end, bbInflation);
            if (hit.getType() != HitResult.Type.MISS)
                hits.add(hit);
        }

        if (!hits.isEmpty()) {
            hits.sort(Comparator.comparingDouble(o -> o.getLocation().distanceToSqr(start)));
            return hits.get(0);
        } else if (checkForBlocks) {
            return blockHitResult;
        }
        return BlockHitResult.miss(end, Direction.UP, BlockPos.containing(end));
    }

    public static void sendParticleCircle(ServerLevel level, Entity entity, SimpleParticleType particleType, float radius, int count) {
        double posX = entity.getX();
        double posY = entity.getY() + entity.getBbHeight() * 0.5;
        double posZ = entity.getZ();

        RandomSource random = RandomSource.create(); // 创建随机数生成器

        for (int i = 0; i < count; i++) {
            // 计算角度
            double angle = i * (2 * Math.PI / count);

            // 在圆周上计算粒子位置
            double offsetX = radius * Math.cos(angle);
            double offsetZ = radius * Math.sin(angle);

            // 添加一点随机偏移，使粒子分散
            offsetX += random.nextDouble() * 0.1 - 0.05;
            offsetZ += random.nextDouble() * 0.1 - 0.05;

            // 发送粒子
            level.sendParticles(particleType, posX + offsetX, posY, posZ + offsetZ, 1, 0.0, 0.0, 0.0, 0.05);
        }
    }

    /**
     * 查找玩家瞄准的非自身、活着的生物实体。
     *
     * @param player 玩家实例
     * @return 找到的实体或空
     */
    public static Optional<LivingEntity> findTargetedEntity(LivingEntity player,float length) {
        // 获取玩家视线方向的射线追踪结果
        HitResult hit = player.pick(5, 1.0f, false);

        // 创建一个包围盒，用于限定搜索范围
        Vec3 eyePos = player.getEyePosition(1.0F); // 获取玩家眼睛位置
        AABB searchBox = new AABB(eyePos, eyePos).inflate(length);

        // 获取玩家周围的实体列表，并过滤出符合条件的实体
        List<LivingEntity> entities = player.level().getEntitiesOfClass(LivingEntity.class, searchBox);

        return entities.stream()
                .filter(entity -> entity != player) // 排除玩家自己
                .filter(entity -> entity.isAlive()) // 只选择存活的实体
                .filter(entity -> entity.getHealth() > 0) // 生命值大于0
                .min((e1, e2) -> Double.compare(e1.distanceToSqr(hit.getLocation()), e2.distanceToSqr(hit.getLocation())));
        // 按照距离射线追踪点的距离排序，取最近的一个
    }
    /**
     * 获取玩家当前指向的实体。
     *
     * @param player 玩家实例
     * @param range 射线的最大距离
     * @return 被击中的实体或null（如果没有实体被击中）
     */
    public static Entity getPointedEntity(Player player, double range) {
        Level world = player.level();
        Vec3 eyePos = player.getEyePosition(1.0F); // 获取玩家的眼睛位置
        Vec3 lookVec = player.getViewVector(1.0F); // 获取玩家的视线向量
        Vec3 targetPos = eyePos.add(lookVec.x * range, lookVec.y * range, lookVec.z * range); // 计算射线目标位置

        // 设置射线追踪上下文
        ClipContext context = new ClipContext(eyePos, targetPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player);

        // 执行射线追踪
        HitResult hitResult = world.clip(context);

        if (hitResult.getType() == HitResult.Type.MISS) {
            return null; // 如果没有击中任何东西，则返回null
        }

        // 定义一个边界框用于检测实体
        AABB aabb = player.getBoundingBox().expandTowards(lookVec.scale(range)).inflate(1, 1, 1);

        // 查找最近的实体
        Entity pointedEntity = null;
        double closestDist = range;

        for (Entity entity : world.getEntities(player, aabb, e -> !e.isSpectator())) {
            AABB entityBox = entity.getBoundingBox().inflate(entity.getPickRadius());
            Optional<Vec3> optional = entityBox.clip(eyePos, targetPos);

            if (entityBox.contains(eyePos)) {
                if (closestDist >= 0) {
                    pointedEntity = entity;
                    closestDist = 0;
                }
            } else if (optional.isPresent()) {
                double dist = eyePos.distanceToSqr(optional.get());

                if (dist < closestDist) {
                    closestDist = dist;
                    pointedEntity = entity;
                }
            }
        }

        return pointedEntity;
    }
    /**
     * 获取玩家当前指向的最近的LivingEntity。

     /**
     * 获取玩家当前指向的最近的LivingEntity。
     *
     * @param player 玩家实例
     * @param range 射线的最大距离
     * @return 被击中的最近的LivingEntity或null（如果没有LivingEntity被击中）
     */
    public static LivingEntity getPointedLivingEntity(Player player, double range) {
        Level world = player.level();
        Vec3 eyePos = player.getEyePosition(1.0F); // 获取玩家的眼睛位置
        Vec3 lookVec = player.getViewVector(1.0F); // 获取玩家的视线向量
        Vec3 targetPos = eyePos.add(lookVec.x * range, lookVec.y * range, lookVec.z * range); // 计算射线目标位置

        // 设置射线追踪上下文
        ClipContext context = new ClipContext(eyePos, targetPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player);

        // 执行射线追踪并检查是否击中了实体
        HitResult hitResult = world.clip(context);

        if (hitResult.getType() == HitResult.Type.ENTITY) {
            Entity entity = ((EntityHitResult) hitResult).getEntity();
            if (entity instanceof LivingEntity livingEntity && livingEntity != player && livingEntity.getHealth() > 0) {
                return livingEntity;
            }
        }

        // 如果没有直接击中实体，则查找最近的LivingEntity
        AABB aabb = player.getBoundingBox().expandTowards(lookVec.scale(range)).inflate(1, 1, 1);
        LivingEntity pointedLivingEntity = null;
        double closestDist = range;

        for (Entity entity : world.getEntitiesOfClass(LivingEntity.class, aabb, e -> !e.isSpectator() && e != player && e.getHealth() > 0)) {
            Vec3 closestPoint = entity.position().add(0, entity.getBbHeight() / 2, 0); // 实体中心点
            double dist = eyePos.distanceToSqr(closestPoint);

            if (dist < closestDist) {
                Vec3 toEntity = closestPoint.subtract(eyePos);
                if (toEntity.dot(lookVec) > 0) { // 检查实体是否在玩家视线前方
                    closestDist = dist;
                    pointedLivingEntity = (LivingEntity) entity;
                }
            }
        }

        return pointedLivingEntity;
    }
}
